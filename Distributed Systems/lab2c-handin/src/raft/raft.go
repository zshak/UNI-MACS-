package raft

//
// this is an outline of the API that raft must expose to
// the service (or tester). see comments below for
// each of these functions for more details.
//
// rf = Make(...)
//   create a new Raft server.
// rf.Start(command interface{}) (index, term, isleader)
//   start agreement on a new log entry
// rf.GetState() (term, isLeader)
//   ask a Raft for its current term, and whether it thinks it is leader
// ApplyMsg
//   each time a new entry is committed to the log, each Raft peer
//   should send an ApplyMsg to the service (or tester)
//   in the same server.
//

import (
	"6.5840/labgob"
	"bytes"
	"log"

	//	"bytes"
	"math/rand"
	"sync"
	"sync/atomic"
	"time"

	//	"6.5840/labgob"
	"6.5840/labrpc"
)

// as each Raft peer becomes aware that successive log entries are
// committed, the peer should send an ApplyMsg to the service (or
// tester) on the same server, via the applyCh passed to Make(). set
// CommandValid to true to indicate that the ApplyMsg contains a newly
// committed log entry.
//
// in part 2D you'll want to send other kinds of messages (e.g.,
// snapshots) on the applyCh, but set CommandValid to false for these
// other uses.

const (
	FOLLOWER int = iota
	CANDIDATE
	LEADER
)
const ELECTION_LOW = 300
const ELECTION_HIGH = 450

type ApplyMsg struct {
	CommandValid bool
	Command      interface{}
	CommandIndex int

	// For 2D:
	SnapshotValid bool
	Snapshot      []byte
	SnapshotTerm  int
	SnapshotIndex int
}

type AppendEntriesArgs struct {
	Term         int
	LeaderId     int
	PrevLogIndex int
	PrevLogTerm  int
	Entries      []Log
	LeaderCommit int
}

type AppendEntriesReply struct {
	Term    int
	Success bool
}

type Log struct {
	Command interface{}
	Term    int
}

// A Go object implementing a single Raft peer.
type Raft struct {
	mu        sync.Mutex          // Lock to protect shared access to this peer's state
	peers     []*labrpc.ClientEnd // RPC end points of all peers
	persister *Persister          // Object to hold this peer's persisted state
	me        int                 // this peer's index into peers[]
	dead      int32               // set by Kill()
	// Your data here (2A, 2B, 2C).
	// Look at the paper's Figure 2 for a description of what
	// state a Raft server must maintain.
	currentTerm int
	votedFor    int
	log         []Log
	state       int
	commitIndex int //index of highest log entry known to be  committed (initialized to 0, increases monotonically)
	lastApplied int //index of highest log entry applied to state machine (initialized to 0, increases monotonically)

	nextIndex  []int //for each server, index of the next log entry to send to that server (initialized to leader last log index + 1)
	matchIndex []int //for each server, index of highest log entry known to be replicated on server (initialized to 0, increases monotonically)

	applyCh chan ApplyMsg

	lastHeartBeatTime time.Time
}

// return currentTerm and whether this server
// believes it is the leader.
func (rf *Raft) GetState() (int, bool) {

	var term int
	var isleader bool
	// Your code here (2A).
	rf.mu.Lock()
	defer rf.mu.Unlock()
	term = rf.currentTerm
	isleader = rf.state == LEADER
	return term, isleader
}

// save Raft's persistent state to stable storage,
// where it can later be retrieved after a crash and restart.
// see paper's Figure 2 for a description of what should be persistent.
// before you've implemented snapshots, you should pass nil as the
// second argument to persister.Save().
// after you've implemented snapshots, pass the current snapshot
// (or nil if there's not yet a snapshot).
func (rf *Raft) persist() {
	// Your code here (2C).
	// Example:
	// w := new(bytes.Buffer)
	// e := labgob.NewEncoder(w)
	// e.Encode(rf.xxx)
	// e.Encode(rf.yyy)
	// raftstate := w.Bytes()
	// rf.persister.Save(raftstate, nil)
	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)
	e.Encode(rf.currentTerm)
	e.Encode(rf.votedFor)
	e.Encode(rf.log)
	data := w.Bytes()
	rf.persister.Save(data, nil)
}

// restore previously persisted state.
func (rf *Raft) readPersist(data []byte) {
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}
	// Your code here (2C).
	// Example:
	// r := bytes.NewBuffer(data)
	// d := labgob.NewDecoder(r)
	// var xxx
	// var yyy
	// if d.Decode(&xxx) != nil ||
	//    d.Decode(&yyy) != nil {
	//   error...
	// } else {
	//   rf.xxx = xxx
	//   rf.yyy = yyy
	// }
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}
	// Your code here (2C).
	r := bytes.NewBuffer(data)
	d := labgob.NewDecoder(r)
	var currentTerm int
	var votedFor int
	var logs []Log
	if d.Decode(&currentTerm) != nil || d.Decode(&votedFor) != nil || d.Decode(&logs) != nil {
		log.Printf("error")
	} else {
		rf.currentTerm = currentTerm
		rf.votedFor = votedFor
		rf.log = logs
	}
}

// the service says it has created a snapshot that has
// all info up to and including index. this means the
// service no longer needs the log through (and including)
// that index. Raft should now trim its log as much as possible.
func (rf *Raft) Snapshot(index int, snapshot []byte) {
	// Your code here (2D).

}

// example RequestVote RPC arguments structure.
// field names must start with capital letters!
type RequestVoteArgs struct {
	Term         int
	Candidate    int
	LastLogIndex int
	LastLogTerm  int
	// Your data here (2A, 2B).
}

// example RequestVote RPC reply structure.
// field names must start with capital letters!
type RequestVoteReply struct {
	// Your data here (2A).
	Term        int
	VoteGranted bool
}

// example RequestVote RPC handler.
func (rf *Raft) RequestVote(args *RequestVoteArgs, reply *RequestVoteReply) {

	rf.mu.Lock()
	reply.Term = rf.currentTerm

	//me ufro magari var
	if args.Term < rf.currentTerm {
		reply.VoteGranted = false
		rf.mu.Unlock()
		return
	}

	if args.Term > rf.currentTerm {
		rf.state = FOLLOWER
		rf.votedFor = -1
		rf.currentTerm = args.Term
		reply.VoteGranted = true
		rf.persist()
	}

	if rf.votedFor < 0 || rf.votedFor == args.Candidate {

		lastLogTerm := rf.log[len(rf.log)-1].Term

		if args.LastLogTerm > lastLogTerm {
			reply.VoteGranted = true
			rf.votedFor = args.Candidate
			rf.state = FOLLOWER
			rf.persist()
			rf.mu.Unlock()
			return
		}

		if args.LastLogTerm == lastLogTerm && (len(rf.log)-1) <= args.LastLogIndex {
			reply.VoteGranted = true
			rf.votedFor = args.Candidate
			rf.state = FOLLOWER
			rf.persist()
			rf.mu.Unlock()
			return
		}
	}
	reply.VoteGranted = false
	rf.mu.Unlock()
}

// example code to send a RequestVote RPC to a server.
// server is the index of the target server in rf.peers[].
// expects RPC arguments in args.
// fills in *reply with RPC reply, so caller should
// pass &reply.
// the types of the args and reply passed to Call() must be
// the same as the types of the arguments declared in the
// handler function (including whether they are pointers).
//
// The labrpc package simulates a lossy network, in which servers
// may be unreachable, and in which requests and replies may be lost.
// Call() sends a request and waits for a reply. If a reply arrives
// within a timeout interval, Call() returns true; otherwise
// Call() returns false. Thus Call() may not return for a while.
// A false return can be caused by a dead server, a live server that
// can't be reached, a lost request, or a lost reply.
//
// Call() is guaranteed to return (perhaps after a delay) *except* if the
// handler function on the server side does not return.  Thus there
// is no need to implement your own timeouts around Call().
//
// look at the comments in ../labrpc/labrpc.go for more details.
//
// if you're having trouble getting RPC to work, check that you've
// capitalized all field names in structs passed over RPC, and
// that the caller passes the address of the reply struct with &, not
// the struct itself.
func (rf *Raft) sendRequestVote(server int, args *RequestVoteArgs, reply *RequestVoteReply) bool {
	ok := rf.peers[server].Call("Raft.RequestVote", args, reply)
	return ok
}

// the service using Raft (e.g. a k/v server) wants to start
// agreement on the next command to be appended to Raft's log. if this
// server isn't the leader, returns false. otherwise start the
// agreement and return immediately. there is no guarantee that this
// command will ever be committed to the Raft log, since the leader
// may fail or lose an election. even if the Raft instance has been killed,
// this function should return gracefully.
//
// the first return value is the index that the command will appear at
// if it's ever committed. the second return value is the current
// term. the third return value is true if this server believes it is
// the leader.
func (rf *Raft) Start(command interface{}) (int, int, bool) {
	index := -1
	term := -1
	isLeader := true

	rf.mu.Lock()
	state := rf.state
	if state != LEADER {
		isLeader = false
		rf.mu.Unlock()
		return index, term, isLeader
	}

	index = len(rf.log)
	term = rf.currentTerm
	Log := Log{Command: command, Term: rf.currentTerm}
	rf.log = append(rf.log, Log)
	rf.nextIndex[rf.me] += 1
	rf.matchIndex[rf.me] = index
	rf.persist()
	//rf.ReplicateLog()
	rf.mu.Unlock()
	// Your code here (2B).

	return index, term, isLeader
}

func (rf *Raft) ReplicateLog() {
	for peer, _ := range rf.peers {
		//log.Printf("REPLICATELOG:  peer - %v term -%v", peer, rf.currentTerm)
		if peer == rf.me {
			//log.Printf("me - %v", rf.me)
			continue
		}
		go func(peer int) {
			rf.ReplicateLogOnPeer(peer)
		}(peer)
	}
}

func (rf *Raft) ReplicateLogOnPeer(peer int) {
	for {
		//log.Printf("REPLICATELOGONPEER - shemovedo peer -%v", peer)
		//time.Sleep(10 * time.Millisecond)
		rf.mu.Lock()

		if rf.killed() || rf.state != LEADER {
			rf.mu.Unlock()
			return
		}
		//Arguments
		term := rf.currentTerm
		leaderId := rf.me
		index := rf.nextIndex[peer]
		prevLogIndex := index - 1
		//log.Printf("prevlogindex- %v, log -%v", prevLogIndex, rf.log)
		prevLogTerm := rf.log[prevLogIndex].Term
		entries := rf.log[rf.nextIndex[peer]:]
		leaderCommit := rf.commitIndex
		//log.Printf("ReplicateLogOnPeer: peer- %v, index - %v, prevLogIndex - %v, prevLogTerm - %v, leaderCommit -%v ENTRIES - ", peer, index, prevLogIndex, prevLogTerm, leaderCommit)
		//for _, l := range entries {
		//	log.Print(l)
		//}
		//log.Printf("")

		rf.mu.Unlock()

		//time.Sleep(10 * time.Millisecond)
		appendEntriesRequest := AppendEntriesArgs{
			Term:         term,
			LeaderId:     leaderId,
			PrevLogIndex: prevLogIndex,
			PrevLogTerm:  prevLogTerm,
			Entries:      entries,
			LeaderCommit: leaderCommit,
		}

		appendEntriesResponse := AppendEntriesReply{}

		rf.mu.Lock()
		//log.Printf("vai - %v", rf.state)
		if rf.state != LEADER {
			rf.mu.Unlock()
			return
		}
		rf.mu.Unlock()
		rf.peers[peer].Call("Raft.AppendEntriesHandler", &appendEntriesRequest, &appendEntriesResponse)

		rf.mu.Lock()
		//if rf.currentTerm != appendEntriesRequest.Term {
		//	rf.mu.Unlock()
		//	return
		//}

		if rf.currentTerm != appendEntriesRequest.Term {

			rf.mu.Unlock()
			return
		}

		//log.Printf("ReplicateLogOnPeer: success -%v", appendEntriesResponse.Success)
		if appendEntriesResponse.Success {
			rf.matchIndex[peer] = prevLogIndex + len(entries)
			//log.Printf("success : matchindex[%v] - %v", peer, rf.matchIndex[peer])
			rf.nextIndex[peer] = rf.matchIndex[peer] + 1
			rf.mu.Unlock()
			//rf.applyToChannel()
			return
		}

		if appendEntriesResponse.Term > rf.currentTerm {
			rf.state = FOLLOWER
			rf.currentTerm = appendEntriesResponse.Term
			rf.persist()
			rf.mu.Unlock()
			return
		}

		//fail
		rf.nextIndex[peer] -= 1
		if rf.nextIndex[peer] < 1 {
			rf.nextIndex[peer] = 1
		}
		rf.mu.Unlock()
	}

}

// the tester doesn't halt goroutines created by Raft after each test,
// but it does call the Kill() method. your code can use killed() to
// check whether Kill() has been called. the use of atomic avoids the
// need for a lock.
//
// the issue is that long-running goroutines use memory and may chew
// up CPU time, perhaps causing later tests to fail and generating
// confusing debug output. any goroutine with a long-running loop
// should call killed() to check whether it should stop.
func (rf *Raft) Kill() {
	atomic.StoreInt32(&rf.dead, 1)
	// Your code here, if desired.
}

func (rf *Raft) killed() bool {
	z := atomic.LoadInt32(&rf.dead)
	return z == 1
}

func (rf *Raft) runAsFollower() {
	statedWaitingAt := time.Now()
	waitForHeartbeat := ELECTION_LOW + rand.Intn(ELECTION_HIGH-ELECTION_LOW)
	//log.Printf("runAsFollower: server %v waiting for %v ms\n", rf.me, electionTimeout)
	time.Sleep(time.Duration(waitForHeartbeat) * time.Millisecond)

	rf.mu.Lock()
	//log.Printf("runAsFollower: server %v aquired lock", rf.me)
	state := rf.state
	//log.Printf("runAsFollower: server %v state: %v", rf.me, state)

	if state != FOLLOWER {
		//log.Printf("runAsFollower: server %v state changed to %v", rf.me, rf.state)
		rf.mu.Unlock()
		return
	}

	if statedWaitingAt.After(rf.lastHeartBeatTime) {
		//log.Printf("runAsFollower: server %v became Candidate", rf.me)
		rf.state = CANDIDATE
	}
	//log.Printf("runAsFollower: server %v unlocked lock", rf.me)
	rf.mu.Unlock()
}

func (rf *Raft) startElection(request *RequestVoteArgs) {
	//log.Printf("startElection: starting election for server %v", rf.me)
	votes := 1
	defer rf.persist()
	for peer := range rf.peers {
		if peer == rf.me {
			continue
		}
		go func(peer int) {
			rf.mu.Lock()
			//log.Printf("startElection asking for votes: server %v aquired lock", rf.me)
			if rf.state != CANDIDATE {
				rf.mu.Unlock()

				//log.Printf("startElection asking for votes: server %v state changed to %v during asking for votes", rf.me, rf.state)
				return
			}
			//log.Printf("startElection asking for votes: server %v unlocked lock", rf.me)
			rf.mu.Unlock()
			//log.Printf("startElection asking for votes: server %v asking for vote to %v", rf.me, peer)
			response := RequestVoteReply{}
			ok := rf.sendRequestVote(peer, request, &response)
			if ok && response.VoteGranted {
				//log.Printf("startElection asking for votes: server %v granted vote to %v", peer, rf.me)
				rf.mu.Lock()
				votes++
				rf.mu.Unlock()
			} else {
				//continue
			}

		}(peer)
	}

	time.Sleep(100 * time.Millisecond)

	rf.mu.Lock()
	if rf.state != CANDIDATE {
		rf.mu.Unlock()
		return
	}
	rf.mu.Unlock()

	rf.mu.Lock()
	if votes > len(rf.peers)/2 {
		rf.state = LEADER
		rf.votedFor = rf.me
		for peer := range rf.peers {
			rf.nextIndex[peer] = len(rf.log)
			rf.matchIndex[peer] = 0
		}
		//log.Printf("startElection: server %v became a leader, sending heartBeat", rf.me)
		rf.mu.Unlock()
		rf.ReplicateLog()
	} else {
		rf.mu.Unlock()
	}
}

func (rf *Raft) runAsCandidate() {
	rf.mu.Lock()
	//log.Printf("runAsCandidate: server %v aquired lock", rf.me)

	if rf.state != CANDIDATE {
		//log.Printf("runAsCandidate: server %v state changed to %v", rf.me, rf.state)
		rf.mu.Unlock()
		return
	}
	lastLogTerm := 0
	if len(rf.log) > 0 {
		lastLogTerm = rf.log[len(rf.log)-1].Term
	}
	rf.currentTerm++
	rf.votedFor = rf.me
	rf.persist()
	request := RequestVoteArgs{
		Term:         rf.currentTerm,
		Candidate:    rf.me,
		LastLogIndex: len(rf.log),
		LastLogTerm:  lastLogTerm,
	}

	//log.Printf("runAsCandidate: server %v unlocked lock", rf.me)
	rf.mu.Unlock()

	rf.startElection(&request)
}

//rf.mu.Lock()
////log.Printf("sendHeartBeat: server %v aquired lock", rf.me)
//if rf.state != LEADER {
////log.Printf("sendHearBeat: server %v changed state to %v", rf.me, rf.state)
//rf.mu.Unlock()
//return
//}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

//Term         int
//LeaderId     int
//PrevLogIndex int
//PrevLogTerm  int
//Entries      []Log
//LeaderCommit int

func (rf *Raft) AppendEntriesToLog(args *AppendEntriesArgs) {
	rf.log = rf.log[:args.PrevLogIndex+1]
	rf.log = append(rf.log, args.Entries...)
	//log.Printf("APPENDENTRIESTOLOG: server -%v, args - %v updatedLog - %v ", rf.me, *args, rf.log)
}

func (rf *Raft) AppendEntriesHandler(args *AppendEntriesArgs, reply *AppendEntriesReply) {
	//log.Printf("appendEntriesHandler: server %v received from %v", rf.me, args.LeaderId)
	rf.mu.Lock()
	defer rf.persist()
	//log.Printf("appendEntriesHandler: server %v Acquired Lock", rf.me)
	reply.Term = rf.currentTerm
	//log.Printf("APPENDENTRIESHANDLER: server -%v, state - %v chemiTerm - %v, args - %v, chemiLog - %v", rf.me, rf.state, rf.currentTerm, *args, rf.log)
	if args.Term < rf.currentTerm {
		reply.Success = false
		//log.Printf("appendEntriesHandler: server %v unlocked Lock i had higher term", rf.me)
		rf.mu.Unlock()
		return
	}

	//aq ukve lideris termi >= chems

	rf.state = FOLLOWER
	rf.currentTerm = args.Term
	//rf.votedFor = args.LeaderId
	rf.lastHeartBeatTime = time.Now()
	rf.votedFor = -1

	//if args.PrevLogIndex < 0 {
	//	DPrintf("entries")
	//	for i := 0; i < len(args.Entries); i++ {
	//		DPrintf("%v: log: %v", i, args.Entries[i])
	//	}
	//	copy(rf.log, args.Entries)
	//	reply.Success = true
	//	rf.commitIndex = min(args.LeaderCommit, rf.commitIndex)
	//	rf.mu.Unlock()
	//	rf.applyToChannel()
	//	return
	//}

	if args.PrevLogIndex >= len(rf.log) {

		reply.Success = false
		rf.mu.Unlock()
		return
	}

	if rf.log[args.PrevLogIndex].Term != args.PrevLogTerm {
		reply.Success = false
		//rf.log = rf.log[:args.PrevLogIndex]
		//sheidzleba truncate
		//log.Printf("appendEntriesHandler: server %v unlocked Lock i had higher term", rf.me)
		rf.mu.Unlock()
		return
	}
	reply.Success = true
	if args.LeaderCommit > rf.commitIndex {
		rf.commitIndex = min(args.LeaderCommit, args.PrevLogIndex+len(args.Entries))
	}
	rf.AppendEntriesToLog(args)

	//log.Printf("appendEntriesHandler: server %v Heartbeat received at %v", rf.me, time.Now())

	//log.Printf("appendEntriesHandler: server %v unlocked Lock", rf.me)
	rf.mu.Unlock()
	//rf.applyToChannel()

}

func (rf *Raft) sendHeartBeat() {

	for peer := range rf.peers {
		if peer == rf.me {
			continue
		}

		go func(peer int) {
			response := AppendEntriesReply{}
			rf.mu.Lock()
			//log.Printf("sendHearBeat sending heartbeats: server %v, peer %v aquired lock", rf.me, peer)

			if rf.state != LEADER {
				//log.Printf("sendHearBeat sending heartbeats: server %v, peer %v changed state to %v", rf.me, peer, rf.state)
				rf.mu.Unlock()
				return
			}
			index := rf.nextIndex[peer]
			prevLogIndex := index - 1
			prevLogTerm := rf.log[prevLogIndex].Term
			entries := rf.log[rf.nextIndex[peer]:]
			leaderCommit := rf.commitIndex

			request := AppendEntriesArgs{
				Term:         rf.currentTerm,
				LeaderId:     rf.me,
				PrevLogIndex: prevLogIndex,
				PrevLogTerm:  prevLogTerm,
				Entries:      entries,
				LeaderCommit: leaderCommit,
			}

			//log.Printf("sendHearBeat sending heartbeats: server %v, peer %v unlocked", rf.me, peer)
			rf.mu.Unlock()
			//log.Printf("sendHearBeat sending heartbeats: server %v, peer %v aquired lock sending appendEntry", rf.me, peer)

			rf.peers[peer].Call("Raft.AppendEntriesHandler", &request, &response)

		}(peer)
	}
}

func (rf *Raft) runAsLeader() {
	rf.mu.Lock()
	//log.Printf("runAsLeader: server %v aquired lock", rf.me)
	if rf.state != LEADER {
		//log.Printf("runAsLeader: server %v state changed to %v", rf.me, rf.state)
		rf.mu.Unlock()
		return
	}
	rf.mu.Unlock()
	rf.ReplicateLog()
	rf.UpdateCommitIndex()
}

func (rf *Raft) CountPeersReplicatedIndex(potentialCommitIndex int) int {
	res := 0
	for peer := range rf.peers {
		if rf.me == peer {
			continue
		}

		if rf.log[potentialCommitIndex].Term != rf.currentTerm {
			continue
		}
		if rf.matchIndex[peer] >= potentialCommitIndex {
			res++
		}
	}
	return res
}

func (rf *Raft) UpdateCommitIndex() {
	rf.mu.Lock()
	for N := rf.commitIndex + 1; N < len(rf.log); N++ {
		numPeersToSatisfy := 1 + rf.CountPeersReplicatedIndex(N)
		if numPeersToSatisfy > len(rf.matchIndex)/2 {
			rf.commitIndex = N
			rf.mu.Unlock()
			return
		}
	}
	//log.Printf("leader scommit index: %v", rf.commitIndex)
	rf.mu.Unlock()
}

func (rf *Raft) ticker() {
	for rf.killed() == false {
		rf.mu.Lock()
		//log.Printf("ticker: server %v aquired lock to check state", rf.me)
		state := rf.state
		//log.Printf("ticker: server %v unlocked lock, state: %v", rf.me, rf.state)
		rf.mu.Unlock()
		switch state {
		case FOLLOWER:
			//log.Printf("ticker, follower: server %v TERM: %v", rf.me, rf.currentTerm)
			rf.runAsFollower()
		case CANDIDATE:
			//log.Printf("ticker, candidate: server %v TERM: %v", rf.me, rf.currentTerm)
			rf.runAsCandidate()
		case LEADER:
			//log.Printf("ticker, leader: server %v TERM: %v", rf.me, rf.currentTerm)
			rf.runAsLeader()
			time.Sleep(time.Duration(100) * time.Millisecond)
		}
		// Your code here (2A)
		// Check if a leader election should be started.

	}
}

func (rf *Raft) TryApply() {
	for rf.killed() == false {
		rf.mu.Lock()

		//log.Printf("TRYAPPLY: lastApplied - %v, commitIndex - %v, state - %v", rf.lastApplied, rf.commitIndex, rf.state)
		if rf.lastApplied >= rf.commitIndex {
			rf.mu.Unlock()
			time.Sleep(20 * time.Millisecond)
			continue
		}

		LogsToApply := rf.log[rf.lastApplied+1 : rf.commitIndex+1]

		for ind, l := range LogsToApply {
			//log.Printf("index - %v", rf.lastApplied+1+ind)
			rf.applyCh <- ApplyMsg{CommandValid: true, Command: l.Command, CommandIndex: rf.lastApplied + 1 + ind}
		}
		rf.lastApplied = rf.commitIndex
		rf.mu.Unlock()
		time.Sleep(20 * time.Millisecond)
	}
}

// the service or tester wants to create a Raft server. the ports
// of all the Raft servers (including this one) are in peers[]. this
// server's port is peers[me]. all the servers' peers[] arrays
// have the same order. persister is a place for this server to
// save its persistent state, and also initially holds the most
// recent saved state, if any. applyCh is a channel on which the
// tester or service expects Raft to send ApplyMsg messages.
// Make() must return quickly, so it should start goroutines
// for any long-running work.
func Make(peers []*labrpc.ClientEnd, me int,
	persister *Persister, applyCh chan ApplyMsg) *Raft {
	rf := &Raft{}
	rf.peers = peers
	rf.persister = persister
	rf.me = me
	rf.applyCh = applyCh
	// Your initialization code here (2A, 2B, 2C).
	rf.lastApplied = 0
	rf.currentTerm = 0
	rf.commitIndex = 0
	rf.votedFor = -1
	rf.log = make([]Log, 1)
	rf.matchIndex = make([]int, len(peers))
	// for i := 0; i < len(rf.peers); i++ {
	//     rf.matchIndex[i] = -1
	// }
	rf.nextIndex = make([]int, len(peers))
	for i := 0; i < len(rf.nextIndex); i++ {
		rf.nextIndex[i] = 1
	}
	rf.lastHeartBeatTime = time.Now()

	rf.state = FOLLOWER
	// initialize from state persisted before a crash
	rf.readPersist(persister.ReadRaftState())

	//log.Printf("server %v started\n", rf.me)

	go rf.ticker()
	go rf.TryApply()
	return rf
}
