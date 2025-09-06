package mr

import (
	"log"
	"net"
	"net/http"
	"net/rpc"
	"os"
	"sync"
	"time"
)

const (
	NOT_STARTED = iota
	IN_PROGRESS
	Finished
)

type Coordinator struct {
	map_tasks    map[string]int //status
	reduce_tasks map[int]int    //status
	map_tasks_id map[string]int //map task number
	n_reduce     int
	n_map        int
	lock         sync.Mutex
}

// Your code here -- RPC handlers for the worker to call.

//
// an example RPC handler.
//
// the RPC argument and reply types are defined in rpc.go.
//
func (c *Coordinator) Example(args *ExampleArgs, reply *ExampleReply) error {
	reply.Y = args.X + 1
	return nil
}

func (c *Coordinator) AssignTask(request *TaskRequest, response *TaskResponse) error {
	//fmt.Println("assigning")
	c.lock.Lock()
	defer c.lock.Unlock()

	if !mappingDone(c) {
		filename := getFileToMap(c)
		if filename == "" {
			response.Type = NONE
			return nil
		}
		c.map_tasks[filename] = IN_PROGRESS
		response.Type = MAP
		response.TaskId = c.map_tasks_id[filename]
		response.MapTask = MapJob{}
		response.MapTask.NumReducers = c.n_reduce
		response.MapTask.InputFile = filename
		go MonitorWorkerMap(c, response)
		return nil
	} else if !reduceDone(c) {
		id := getReducerTaskId(c)
		if id == -1 {
			response.Type = NONE
			return nil
		}
		response.Type = REDUCE
		response.TaskId = id
		response.ReduceTask = ReduceJob{}
		response.ReduceTask.NumMappers = c.n_map
		go MonitorWorkerReduce(c, response)
		return nil
	}

	response.Type = DONE
	response.Dead = false
	return nil
}

func MonitorWorkerMap(c *Coordinator, args *TaskResponse) {
	time.Sleep(10 * time.Second)
	//fmt.Printf("moxxdaa")
	c.lock.Lock()
	defer c.lock.Unlock()

	if c.map_tasks[args.MapTask.InputFile] != Finished {
		args.lock.Lock()
		args.Dead = true
		args.lock.Unlock()
		c.map_tasks[args.MapTask.InputFile] = NOT_STARTED
	}
}

func MonitorWorkerReduce(c *Coordinator, args *TaskResponse) {
	time.Sleep(10 * time.Second)
	c.lock.Lock()
	defer c.lock.Unlock()

	if c.reduce_tasks[args.TaskId] != Finished {
		args.lock.Lock()
		args.Dead = true
		args.lock.Unlock()
		c.reduce_tasks[args.TaskId] = NOT_STARTED
	}
}

func getReducerTaskId(c *Coordinator) int {
	for k, v := range c.reduce_tasks {
		if v == NOT_STARTED {
			return k
		}
	}
	return -1
}

func getFileToMap(c *Coordinator) string {
	for k, v := range c.map_tasks {
		if v == NOT_STARTED {
			return k
		}
	}
	return ""
}

func reduceDone(c *Coordinator) bool {
	for _, v := range c.reduce_tasks {
		if v == NOT_STARTED || v == IN_PROGRESS {
			return false
		}
	}
	return true
}

func mappingDone(c *Coordinator) bool {
	for _, v := range c.map_tasks {
		if v == IN_PROGRESS || v == NOT_STARTED {
			return false
		}
	}
	return true
}

//
// start a thread that listens for RPCs from worker.go
//
func (c *Coordinator) server() {
	rpc.Register(c)
	rpc.HandleHTTP()
	//l, e := net.Listen("tcp", ":1234")
	sockname := coordinatorSock()
	os.Remove(sockname)
	l, e := net.Listen("unix", sockname)
	if e != nil {
		log.Fatal("listen error:", e)
	}
	go http.Serve(l, nil)
}

//
// main/mrcoordinator.go calls Done() periodically to find out
// if the entire job has finished.
//
func (c *Coordinator) Done() bool {

	// Your code here.
	c.lock.Lock()
	defer c.lock.Unlock()

	return reduceDone(c)
}

func (c *Coordinator) JobReport(args *TaskResponse, reply *TaskResponse) error {
	args.lock.Lock()
	if args.Dead == true {
		args.lock.Unlock()
		return nil
	}
	args.lock.Unlock()
	c.lock.Lock()
	defer c.lock.Unlock()
	if args.Type == MAP {
		file := args.MapTask.InputFile
		c.map_tasks[file] = Finished
	} else if args.Type == REDUCE {
		reducerId := args.TaskId
		c.reduce_tasks[reducerId] = Finished
	}
	return nil
}

//
// create a Coordinator.
// main/mrcoordinator.go calls this function.
// nReduce is the number of reduce tasks to use.
//
func MakeCoordinator(files []string, nReduce int) *Coordinator {
	c := Coordinator{}
	//fmt.Printf("gavakete koordinatori")
	// Your code here.
	n_maps := len(files)
	c.n_reduce = nReduce
	c.n_map = n_maps
	c.reduce_tasks = make(map[int]int)
	c.map_tasks = make(map[string]int)
	c.map_tasks_id = make(map[string]int)
	for id, file := range files {
		c.map_tasks[file] = NOT_STARTED
		c.map_tasks_id[file] = id
	}

	for i := 0; i < c.n_reduce; i++ {
		c.reduce_tasks[i] = NOT_STARTED
	}
	c.server()
	return &c
}
