
#include "bits/stdc++.h"
using namespace std;

#ifdef _WIN32
#define PATH_SEPARATOR '\\'
#else
#define PATH_SEPARATOR '/'
#endif

unordered_map<int,unordered_map<string,vector<int>>> dfa;
unordered_map<int, bool> accept_states;
string input;
int num_states, num_accept_states, num_transitions;
vector<int> res;

void process_input(){
    cin>>input;
    cin>>num_states>>num_accept_states>>num_transitions;
    for(int i = 0; i < num_accept_states; i++){
        int accept_state;
        cin>>accept_state;
        accept_states[accept_state] = true;
    }

    for(int i = 0; i < num_states; i++){
        int cur_num_transition;
        cin>>cur_num_transition;
        for(int j = 0; j < cur_num_transition; j++){
            int to_state;
            string symbol;
            cin>>symbol>>to_state;
            dfa[i][symbol].push_back(to_state);
        }
    }

}

void simulate(){
    int starting_state = 0;

    int cur_input_index = 0;
    queue<int> cur_states;
    cur_states.push(starting_state);
    vector<bool> cur_visited_states(num_states, false);
    while(!cur_states.empty()){
        // cout<<(cur_input_index)<<endl;
        if(cur_input_index == input.length()) return;
        int cur_num_states = cur_states.size();
        string cur_char = input.substr(cur_input_index,1);
        bool is_accepted = false;
        cur_visited_states = vector<bool>(num_states, false);
        for(int i = 0; i < cur_num_states; i++){
            int cur_state = cur_states.front();
            cur_states.pop();
            vector<int> neighs = dfa[cur_state][cur_char];
            for(int num_neighs = 0; num_neighs < neighs.size(); num_neighs++){
                int cur = neighs[num_neighs];
                if(cur_visited_states[cur]) continue;
                cur_states.push(neighs[num_neighs]);
                if(accept_states[neighs[num_neighs]]) is_accepted = true;
                // cout<<"karoche"<<neighs[num_neighs]<<endl;
                cur_visited_states[neighs[num_neighs]] = true;
                // cout<<cur_visited_states[3]<<"ha"<<endl;
            }
            // cout<<endl;
        }
        res[cur_input_index] = is_accepted;
        is_accepted = false;
        cur_input_index++;
    }
}


void print_map(){
    for(const auto& pair : dfa){
        cout<<"state: "<<pair.first<< " neighs: ";
        for(const auto& neighs : pair.second){
            for(int i = 0; i < neighs.second.size(); i++){
                cout<<"("<< neighs.first<< ","<< neighs.second[i]<< ")"<<endl; 
            }
        }
    }
}


vector<string> split(string word){
        stringstream ss(word);
        vector<string> words;
        while (getline(ss, word, ' ')) {
            words.push_back(word);
        }
        return words;
}

void reset(){
    dfa.clear();
    accept_states.clear();
    res.clear();
}


int count_n = 0;

// void writeInFile(){
//     string res_s = "";
//     for(int i = 0; i < res.size(); i++){
//         if(res[i] == 0){
//             res_s += "N";
//         }else{
//             res_s += "Y";
//         }
//     }

//     string temp = to_string(count_n);
//     if(temp.length() == 1) temp = "0" + temp;
//     string filename = "out" + temp + ".txt";
//     string path = "D:\\kai semestri\\vsCode\\c++\\Public tests\\P2\\res" + string(1, PATH_SEPARATOR);; // replace with the actual path to the directory
//     ofstream outfile(path + filename, ofstream::binary); // create a new file in the specified directory
//     if (outfile.is_open()) {
        
//         outfile << res_s << endl;
//         outfile.close();
//     } else {
//         cout << "Unable to open file." << endl;
//     }
//     count_n++;

// }


// void test(){
//     string folderPath = "D:\\kai semestri\\vsCode\\c++\\Public tests\\P2\\In (public)";  
//     for (const auto& file : std::filesystem::directory_iterator(folderPath)) {
//         reset();
//         // if(count == 1) continue;
//         if (file.is_regular_file()) {
//             if (file.is_regular_file()) {
//             ifstream ifs(file.path());
//             if (ifs.is_open()) {
//                 cout << "Reading file: " << file.path().filename() << endl;

//                 string line;
//                 getline(ifs, line);
//                 input = line;

//                 vector<string> split_line;
//                 getline(ifs, line);
//                 split_line = split(line);
//                 num_states = stoi(split_line[0]);
//                 num_accept_states = stoi(split_line[1]);
//                 num_transitions = stoi(split_line[2]);
                
//                 getline(ifs, line);
//                 split_line = split(line);
//                 for(int i = 0; i < split_line.size(); i++){
//                     accept_states[stoi(split_line[i])] = true;
//                 }
//                 int index = 0;
//                 while (getline(ifs, line)) {
//                     split_line = split(line);
//                     for(int i = 1; i < split_line.size(); i+=2){
//                         string symbol = split_line[i];
//                         int to_state = stoi(split_line[i + 1]);
//                         dfa[index][symbol.c_str()].push_back(to_state);
//                     }
//                     index++;
//                 }
//                 res = vector<int>(input.length());
//                 simulate();
//                 writeInFile();
//                 ifs.close();
//             }
//         }
//         }
//     }
// }

int main() {
    process_input();
    // print_map();
    res = vector<int>(input.length());
    // test();
    simulate();
    for(int i = 0; i < res.size(); i++){
        if(res[i] == 0){
            cout<<"N";
        }else{
            cout<<"Y";
        }
    }
    cout<<"\n";
}