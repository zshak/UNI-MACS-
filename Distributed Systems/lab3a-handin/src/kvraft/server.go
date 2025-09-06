package kvraft

import (
	"6.5840/labgob"
	"6.5840/labrpc"
	"6.5840/raft"
	"log"
	"sync"
	"sync/atomic"
	"time"
)

const Debug = false

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug {
		log.Printf(format, a...)
	}
	return
}

type Op struct {
	// Your definitions here.
	// Field names must start with capital letters,
	// otherwise RPC will break.
	Method  string
	Key     string
	Value   string
	Client  int64
	Request int64
}

type KVServer struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg
	dead    int32 // set by Kill()

	maxraftstate int // snapshot if log grows this big

	// Your definitions here.
	ChMap   map[int]chan Op
	DB      map[string]string
	LastReq map[int64]int64
}

func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	// Your code here.
	_, isLeader := kv.rf.GetState()
	if !isLeader {
		reply.Err = ErrWrongLeader
		return
	}

	command := Op{
		Method:  "Get",
		Key:     args.Key,
		Client:  args.Client,
		Request: args.Request,
	}
	index, _, _ := kv.rf.Start(command)

	kv.mu.Lock()

	ch, ok := kv.ChMap[index]

	if !ok {
		ch = make(chan Op, 1)
		kv.ChMap[index] = ch
	}
	kv.mu.Unlock()
	select {
	case appOp := <-ch:
		if appOp.Client == command.Client && appOp.Request == command.Request {
			reply.Err = OK
			reply.Value = appOp.Value
			return
		} else {
			reply.Err = ErrWrongLeader
			return
		}

	case <-time.After(550 * time.Millisecond):
		return
	}
}

func (kv *KVServer) PutAppend(args *PutAppendArgs, reply *PutAppendReply) {
	_, isLeader := kv.rf.GetState()
	if !isLeader {
		reply.Err = ErrWrongLeader
		return
	}

	command := Op{
		Method:  args.Op,
		Key:     args.Key,
		Value:   args.Value,
		Client:  args.Client,
		Request: args.Request,
	}
	index, _, _ := kv.rf.Start(command)

	kv.mu.Lock()

	ch, ok := kv.ChMap[index]

	if !ok {
		ch = make(chan Op, 1)
		kv.ChMap[index] = ch
	}
	kv.mu.Unlock()
	select {
	case appOp := <-ch:
		if appOp.Client == command.Client && appOp.Request == command.Request {
			reply.Err = OK

			return
		} else {
			reply.Err = ErrWrongLeader
			return
		}

	case <-time.After(550 * time.Millisecond):
		reply.Err = ErrWrongLeader
		return
	}
}

// the tester calls Kill() when a KVServer instance won't
// be needed again. for your convenience, we supply
// code to set rf.dead (without needing a lock),
// and a killed() method to test rf.dead in
// long-running loops. you can also add your own
// code to Kill(). you're not required to do anything
// about this, but it may be convenient (for example)
// to suppress debug output from a Kill()ed instance.
func (kv *KVServer) Kill() {
	atomic.StoreInt32(&kv.dead, 1)
	kv.rf.Kill()
	// Your code here, if desired.
}

func (kv *KVServer) killed() bool {
	z := atomic.LoadInt32(&kv.dead)
	return z == 1
}
func (kv *KVServer) ListenToApply() {
	for !kv.killed() {

		com := <-kv.applyCh

		if !com.CommandValid {

			continue
		}
		op := com.Command.(Op)
		kv.mu.Lock()
		//log.Printf("%v", op.Method)
		if op.Method == "Get" {
			op.Value = kv.DB[op.Key]
		} else {
			req, ok := kv.LastReq[op.Client]
			if !ok || op.Request > req {
				switch op.Method {
				case "Get":
					op.Value = kv.DB[op.Key]
				case "Put":
					kv.DB[op.Key] = op.Value
				case "Append":
					kv.DB[op.Key] += op.Value
				}
				kv.LastReq[op.Client] = op.Request
			}
		}

		ch, ok := kv.ChMap[com.CommandIndex]
		if !ok {
			ch = make(chan Op, 1)
			kv.ChMap[com.CommandIndex] = ch
		}
		ch <- op

		kv.mu.Unlock()
	}
}

// servers[] contains the ports of the set of
// servers that will cooperate via Raft to
// form the fault-tolerant key/value service.
// me is the index of the current server in servers[].
// the k/v server should store snapshots through the underlying Raft
// implementation, which should call persister.SaveStateAndSnapshot() to
// atomically save the Raft state along with the snapshot.
// the k/v server should snapshot when Raft's saved state exceeds maxraftstate bytes,
// in order to allow Raft to garbage-collect its log. if maxraftstate is -1,
// you don't need to snapshot.
// StartKVServer() must return quickly, so it should start goroutines
// for any long-running work.
func StartKVServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister, maxraftstate int) *KVServer {
	// call labgob.Register on structures you want
	// Go's RPC library to marshall/unmarshall.
	labgob.Register(Op{})

	kv := new(KVServer)
	kv.mu.Lock()
	kv.me = me
	kv.maxraftstate = maxraftstate

	// You may need initialization code here.

	kv.applyCh = make(chan raft.ApplyMsg, 1)
	kv.rf = raft.Make(servers, me, persister, kv.applyCh)
	kv.DB = make(map[string]string)
	kv.LastReq = make(map[int64]int64)
	kv.ChMap = make(map[int]chan Op)
	kv.mu.Unlock()
	go kv.ListenToApply()
	// You may need initialization code here.

	return kv
}
