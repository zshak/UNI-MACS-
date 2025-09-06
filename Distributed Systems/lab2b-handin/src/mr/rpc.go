package mr

//
// RPC definitions.
//
// remember to capitalize all names.
//

import (
	"os"
	"sync"
)
import "strconv"

const (
	MAP int = iota
	REDUCE
	NONE
	DONE
)

//
// example to show how to declare the arguments
// and reply for an RPC.
//

type ExampleArgs struct {
	X int
}

type ExampleReply struct {
	Y int
}

// Add your RPC definitions here.

type TaskRequest struct {
}

type TaskResponse struct {
	Type       int
	TaskId     int
	Dead       bool
	MapTask    MapJob
	ReduceTask ReduceJob
	lock       sync.Mutex
}

type MapJob struct {
	NumReducers int
	InputFile   string
}

type ReduceJob struct {
	NumMappers int
}

// Cook up a unique-ish UNIX-domain socket name
// in /var/tmp, for the coordinator.
// Can't use the current directory since
// Athena AFS doesn't support UNIX-domain sockets.
func coordinatorSock() string {
	s := "/var/tmp/5840-mr-"
	s += strconv.Itoa(os.Getuid())
	return s
}
