package mr

import (
	"encoding/json"
	"fmt"
	"hash/fnv"
	"io/ioutil"
	"log"
	"net/rpc"
	"os"
	"strconv"
	"time"
)

//
// Map functions return a slice of KeyValue.
//
type KeyValue struct {
	Key   string
	Value string
}

//
// use ihash(key) % NReduce to choose the reduce
// task number for each KeyValue emitted by Map.
//
func ihash(key string) int {
	h := fnv.New32a()
	h.Write([]byte(key))
	return int(h.Sum32() & 0x7fffffff)
}

//
// main/mrworker.go calls this function.
//
func Worker(mapf func(string, string) []KeyValue,
	reducef func(string, []string) string) {

	// Your worker implementation here.
	//fmt.Printf("oee")
	// uncomment to send the Example RPC to the coordinator.
	// CallExample()

	for {
		response := requestTask()
		if response.Type == MAP {
			DoMap(response, mapf)
		} else if response.Type == REDUCE {
			DoReduce(response, reducef)
		} else if response.Type == NONE {
			time.Sleep(time.Second)
			continue
		} else {
			break
		}
		call("Coordinator.JobReport", &response, &TaskResponse{})
	}
}

func DoReduce(args *TaskResponse, reducef func(string, []string) string) {
	dataToReduce := make(map[string][]string)

	for mapper := 0; mapper < args.ReduceTask.NumMappers; mapper++ {
		fileName := "mr-" + strconv.Itoa(mapper) + "-" + strconv.Itoa(args.TaskId) + ".json"
		file, succ := os.Open(fileName)

		TryCatch(succ, fileName)
		Deserializer := json.NewDecoder(file)
		var model map[string][]string
		err := Deserializer.Decode(&model)
		file.Close()
		if err != nil {
			log.Printf("Can not Deserialize: " + fileName)
		}
		for k, v := range model {
			dataToReduce[k] = append(dataToReduce[k], v...)
		}
	}

	ReducerFilename := "mr-out-" + strconv.Itoa(args.TaskId) + ".txt"
	ReducerOutputFile, err := os.Create(ReducerFilename)
	defer ReducerOutputFile.Close()
	TryCatch(err, ReducerFilename)
	for k, v := range dataToReduce {
		res := reducef(k, v)
		//fmt.Printf(res)
		fmt.Fprintf(ReducerOutputFile, "%v %v\n", k, res)
	}
}

func DoMap(args *TaskResponse, mapf func(string, string) []KeyValue) {
	inputFile := args.MapTask.InputFile
	file, err := os.Open(inputFile)
	TryCatch(err, "Cannot open file %v\n", inputFile)

	content, err := ioutil.ReadAll(file)
	TryCatch(err, "Cannot read file %v\n", inputFile)
	file.Close()

	kva := mapf(inputFile, string(content))
	WriteToIntermediateFiles(args, kva)
}

func WriteToIntermediateFiles(task *TaskResponse, kva []KeyValue) {
	buckets := make([]map[string][]string, task.MapTask.NumReducers)
	for index, _ := range buckets {
		buckets[index] = make(map[string][]string)
	}
	for _, v := range kva {
		bucketIndex := ihash(v.Key) % task.MapTask.NumReducers
		buckets[bucketIndex][v.Key] = append(buckets[bucketIndex][v.Key], v.Value)
	}

	WriteArrayToFile(buckets, task)
}

func WriteArrayToFile(arr []map[string][]string, task *TaskResponse) {
	for index, val := range arr {
		fileName := "mr-" + strconv.Itoa(task.TaskId) + "-" + strconv.Itoa(index) + ".json"
		curDir, _ := os.Getwd()
		tempFile, _ := ioutil.TempFile(curDir, fileName)
		enc := json.NewEncoder(tempFile)
		enc.Encode(&val)
		tempFile.Close()
		os.Rename(tempFile.Name(), fileName)
	}
}

func requestTask() *TaskResponse {
	args := TaskRequest{}
	reply := TaskResponse{}
	call("Coordinator.AssignTask", &args, &reply)
	return &reply
}

//
// example function to show how to make an RPC call to the coordinator.
//
// the RPC argument and reply types are defined in rpc.go.
//
func CallExample() {

	// declare an argument structure.
	args := ExampleArgs{}

	// fill in the argument(s).
	args.X = 99

	// declare a reply structure.
	reply := ExampleReply{}

	// send the RPC request, wait for the reply.
	// the "Coordinator.Example" tells the
	// receiving server that we'd like to call
	// the Example() method of struct Coordinator.
	ok := call("Coordinator.Example", &args, &reply)
	if ok {
		// reply.Y should be 100.
		fmt.Printf("reply.Y %v\n", reply.Y)
	} else {
		fmt.Printf("call failed!\n")
	}
}

func TryCatch(err error, format string, any ...interface{}) {
	if err != nil {
		log.Fatalf(format, any)
	}
}

//
// send an RPC request to the coordinator, wait for the response.
// usually returns true.
// returns false if something goes wrong.
//
func call(rpcname string, args interface{}, reply interface{}) bool {
	// c, err := rpc.DialHTTP("tcp", "127.0.0.1"+":1234")
	sockname := coordinatorSock()
	c, err := rpc.DialHTTP("unix", sockname)
	if err != nil {
		log.Fatal("dialing:", err)
	}
	defer c.Close()

	err = c.Call(rpcname, args, reply)
	if err == nil {
		return true
	}

	fmt.Println(err)
	return false
}
