package kvraft

import (
	"6.5840/labrpc"
	"sync"
)
import "crypto/rand"
import "math/big"

type Clerk struct {
	servers []*labrpc.ClientEnd
	// You will have to modify this struct.
	leader  int
	client  int64
	request int64
	mu      sync.Mutex
}

func nrand() int64 {
	max := big.NewInt(int64(1) << 62)
	bigx, _ := rand.Int(rand.Reader, max)
	x := bigx.Int64()
	return x
}

func MakeClerk(servers []*labrpc.ClientEnd) *Clerk {
	ck := new(Clerk)
	ck.servers = servers
	// You'll have to add code here.
	ck.client = nrand()
	ck.request = 0
	ck.leader = 0

	return ck
}

// fetch the current value for a key.
// returns "" if the key does not exist.
// keeps trying forever in the face of all other errors.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.Get", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) Get(key string) string {
	// You will have to modify this function.
	ck.mu.Lock()
	numServers := len(ck.servers)
	leader := ck.leader
	ck.request++
	args := GetArgs{Key: key, Request: ck.request, Client: ck.client}
	ck.mu.Unlock()

	for {
		//log.Printf("oeeeeeee")
		reply := GetReply{}
		ok := ck.servers[leader].Call("KVServer.Get", &args, &reply)

		if ok && !(reply.Err == ErrWrongLeader) {
			ck.mu.Lock()
			ck.leader = leader
			ck.mu.Unlock()
			if reply.Err == OK {
				return reply.Value
			}
		} else {
			leader = (leader + 1) % numServers
		}
	}
	return ""
}

// shared by Put and Append.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.PutAppend", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) PutAppend(key string, value string, op string) {
	// You will have to modify this function.

	ck.mu.Lock()
	numServers := len(ck.servers)
	leader := ck.leader
	ck.request++
	args := PutAppendArgs{Key: key, Value: value, Op: op, Request: ck.request, Client: ck.client}
	ck.mu.Unlock()

	for {
		reply := PutAppendReply{}
		//log.Printf("%v", leader)
		ok := ck.servers[leader].Call("KVServer.PutAppend", &args, &reply)
		if ok && !(reply.Err == ErrWrongLeader) {
			ck.mu.Lock()
			ck.leader = leader
			ck.mu.Unlock()

			if reply.Err == OK {
				break
			}
		} else {
			leader = (leader + 1) % numServers
		}
	}
}

func (ck *Clerk) Put(key string, value string) {
	ck.PutAppend(key, value, "Put")
}
func (ck *Clerk) Append(key string, value string) {
	ck.PutAppend(key, value, "Append")
}
