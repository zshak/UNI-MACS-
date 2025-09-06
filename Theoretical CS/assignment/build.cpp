#include "bits/stdc++.h"
using namespace std;

unordered_map<int, vector<pair<int,int>>> dfa;
int state_index = 1;

#ifdef _WIN32
#define PATH_SEPARATOR '\\'
#else
#define PATH_SEPARATOR '/'
#endif

bool is_operator(char ch){
    return ch == '|' || ch == '*' || ch == '?';
}

bool has_higher_priority(char ch1, char ch2){
    if(ch1 == '*' && ch2 != '*') return true;
    if(ch1 == '?' && (ch2 != '?' || ch2 != '*')) return true;
    return false;
}

string to_postfix(string str){
    string res = "";
    queue<char> q;
    stack<char> s;
    for(int i = 0; i < str.length(); i++){
        char cur = str[i];
        if(isalpha(cur) || isdigit(cur) || cur == '*'){
            res += cur;
            continue;
        }
        if(is_operator(cur) && s.empty()){
            s.push(cur);
            continue;
        }
        if(is_operator(cur) && s.top() == '('){
            s.push(cur);
            continue;
        }
        if(is_operator(cur)){
            while(true){
                
                if(s.empty()){
                    s.push(cur);
                    break;
                }
                char top_s = s.top();
                if(top_s == '(' || top_s == ')' || isalpha(top_s) || isdigit(top_s) || has_higher_priority(cur, top_s)){
                    s.push(cur);
                    break;
                }
                s.pop();
                res += top_s;
            }  
            continue;
        }

        if(cur == '('){
            s.push(cur);
            continue;
        }
        if(cur == ')'){
            while(true){
                char top_s = s.top();
                s.pop();
                if(top_s == '(') break;
                res += top_s;
            }
        }
    }
    while(!s.empty()){
        char ch = s.top();
        s.pop();
        res += ch;
    }
    return res;
}

bool is_symbol(char ch){
    return isdigit(ch) || isalpha(ch);
}

string add_concat(string reg){
    string res = "";
    for(int i = 0; i < reg.length() - 1; i++){
        char ch = reg[i];

        res += ch;
        if(is_symbol(ch) && (is_symbol(reg[i+1]) || reg[i + 1] == '(')){
            res += "?";
            continue;
        }
        if(ch == ')' && reg[i+1] == '('){
            res += "?";
            continue;
        }
        if(ch == '*' && reg[i+1] == '('){
            res += '?';
            continue;
        }
        if(ch == '*' && is_symbol(reg[i+1])){
            res += "?";
            continue;
        }
        if(ch == ')' && is_symbol(reg[i+1])){
            res += "?";
            continue;
        }
    }
    res += reg[reg.length() - 1];
    return res;
}

struct State {
    int id;
    map<char, set<int>> transitions;
    bool is_start;
    bool is_accept;
};

struct NFA {
    vector<int> states;
    set<int> accept_states;
    set<int> start_states;
};
unordered_map<int,State*> ind_to_state;
set<int>:: iterator it;
set<int>:: iterator it2;
int get_index(){
    int res = state_index;
    state_index++;
    return res;
}


NFA* create_nfa(char ch){
    NFA* res = new NFA;
    State* start = new State;
    start->id = get_index();
    ind_to_state[start->id] = start;
    start->is_start = true;
    start->is_accept = false;
    State* accept = new State;
    accept->id = get_index();
    ind_to_state[accept->id] = accept;
    (start->transitions)[ch].insert(accept->id);
    accept->is_start = false;
    accept->is_accept = true;
    res->accept_states.insert(accept->id);
    res->start_states.insert(start->id);
    res->states.push_back(start->id);
    res->states.push_back(accept->id);
    return res;
}

void concate(NFA* first, NFA* second){
    
    it = first->accept_states.begin();
    while(it != first->accept_states.end()){
        int accept_state = *it;
        State* st = ind_to_state[accept_state];
        st->is_accept = false;
        // cout<<accept_state<<endl;
        for(it2 = second->start_states.begin(); it2 != second->start_states.end(); ++it2){
            int start_state = *it2;
            // cout<<accept_state<<" " << start_state<<endl;
            State* st_state = ind_to_state[start_state];
            st_state->is_start = false;
            // second->start_states.erase(it2);
            // cout<<st->id<<" concate to "<<st_state->id<<endl;
            st->transitions['E'].insert(start_state);
        }
        set<int>::iterator current = it++;
        first->accept_states.erase(current);
        
    }
    for(int i = 0; i < second->states.size(); i++){
        first->states.push_back(second->states[i]);
    }
    for(it = second->accept_states.begin(); it != second->accept_states.end(); ++it){
        first->accept_states.insert(*it);
    }
}

void star(NFA* n){
    
    for(it = n->accept_states.begin(); it != n->accept_states.end(); ++it){
        int accept_ind = *it;
        set<int>::iterator current = it;
        State* st = ind_to_state[accept_ind];
        for(it2 = n->start_states.begin(); it2 != n->start_states.end(); ++it2){
            int start_state = *it2;
            State* st_state = ind_to_state[start_state];
            // cout<<accept_ind<<" to " << start_state<<endl;
            st->transitions['E'].insert(st_state->id);
        }
        // n->accept_states.erase(current);
    }

    for(it = n->start_states.begin(); it != n->start_states.end(); ++it){
        n->accept_states.insert(*it);
    }
}

void unite(NFA* first, NFA* second){
    for(it = second->accept_states.begin(); it != second->accept_states.end(); ++it){
        int accept_state_id = *it;
        first->accept_states.insert(accept_state_id);
    }
    for(it = second->start_states.begin(); it != second->start_states.end(); ++it){
        int start_state_id = *it;
        first->start_states.insert(start_state_id);
    } 
    for(int i = 0; i < second->states.size(); i++){
        first->states.push_back(second->states[i]);
    }
}

NFA* get_nfa(string regex){
    stack<NFA*> s;
    for(int i = 0; i < regex.length(); i++){
        
        if(is_symbol(regex[i])){
            s.push(create_nfa(regex[i]));
            continue;
        }
        char op = regex[i];
        if(op == '?'){
            
            NFA* first = s.top();
            s.pop();

            NFA* second = s.top();
            s.pop();

            concate(second, first);
            // cout<<"push :" << second->accept_states.size()<<endl;
            s.push(second);
            continue;
        }

        if(op == '*'){
            NFA* first = s.top();
            s.pop();
            
            star(first);
            // cout<<"star :" << first->accept_states.size()<<endl;
            s.push(first);
            continue;
        }

        if(op == '|'){
            NFA* second = s.top();
            s.pop();

            NFA* first = s.top();
            s.pop();

            unite(first, second);
            // cout<<"or :" << first->accept_states.size()<<endl;
            s.push(first);
            continue;
        }
    }
    return s.top();
}
// ab*c01|*???*
unordered_map<int, vector<int>> graph;

string nfa_to_string(NFA* res){
    string r = "";
    queue<int> q;
    set<int> s;
    for(it = res->start_states.begin(); it != res->start_states.end(); ++it){
        q.push(*it);
        s.insert(*it);
    }

    int num_transitions = 0;
    while(!q.empty()){
        int num_cur_states = q.size();
        for(int i = 0; i < num_cur_states; i++){
            int st = q.front();
            q.pop();
            State* state = ind_to_state[st];
            // cout<<"ind: "<< st;
            // r = r + to_string(st) + " ";
            int temp_trans = 0;
            string temp_str = "";
            // cout<<st<<" "<< state->transitions.size()<<endl;
            for(const auto& [key,value] : state->transitions){
                num_transitions += value.size();
                temp_trans += value.size();
                for(it = value.begin(); it != value.end(); ++it){
                    if(s.find(*it) == s.end()){
                        s.insert(*it);
                        q.push(*it);
                    }
                    temp_str = temp_str + key + " " + to_string(*it) + " ";
                    // cout<< st<< ", " <<key << "--->" <<*it<<endl;
                }
            }
            r = r + to_string(temp_trans) + " " + temp_str;
            r = r + "\n";
        }
    }
    // cout<<s.size()<<endl;

    string temp = "";
    string accepts = "";
    set<int> real_accept_states;
    for(it = res->accept_states.begin(); it != res->accept_states.end(); ++it){
        if(s.find(*it) != s.end()){
            int temp = *it;
            accepts = accepts + to_string(temp) + " ";
            real_accept_states.insert(temp);
        }
    }

    res->accept_states = real_accept_states;
    temp = to_string(s.size()) + " " + to_string(real_accept_states.size()) + " " + to_string(num_transitions) + "\n";
    // temp = temp + accepts + "\n";

    unordered_map<int,int> m;
    set<int>:: iterator iter;
    int ind = 1;
    for(iter = s.begin(); iter != s.end(); ++iter){
        m[*iter] = ind;
        // cout<<"state : "<<to_string(*iter)<<" "<<ind<<endl;
        ind++;
    }
    stringstream acc_s(accepts);
    string t;
    string tm = "";
    while(getline(acc_s, t, ' ')){
        tm = tm + to_string(m[stoi(t)]) + " ";
    }
    temp = temp + tm + "\n";

    stringstream newliner(r);
    string line;
    string t_s = "";
    while(getline(newliner, line)){
        stringstream ss(line);
        string token;
        bool skip = true;
        getline(ss, token, ' ');
        t_s = t_s + token + " ";
        while(getline(ss, token, ' ')){
            // cout<<"token: "<<token<<endl;
            if(skip){
                t_s = t_s + token + " ";
                skip = false;
                continue;
            }
            // cout<<token<<endl;
            t_s = t_s + to_string(m[stoi(token)]) + " ";
            skip = true;
        }
        t_s += "\n";
    }
    temp = temp + t_s;

    // temp = temp + r;
    return temp;
}

unordered_set<int> visited;

set<int> get_reachable_states(int state_id){
    set<int> res;
    queue<int> q;
    q.push(state_id);
    while(!q.empty()){
        int neigh = q.front();
        q.pop();
        State* st = ind_to_state[neigh];

        for(const auto& [key, value] : st->transitions){
            if(key != 'E') continue;
            for(it = value.begin(); it != value.end(); ++it){
                if(res.find(*it) != res.end()) continue;
                res.insert(*it);
                q.push(*it);
            }
        }
    }
    return res;
}

void deleteE(NFA* n){
    unordered_map<int, set<int>> can_reach_with_e;
    for(int i = 0; i < n->states.size(); i++){
        can_reach_with_e[n->states[i]] = get_reachable_states(n->states[i]);
        // cout<<"size : "<<i<< " "<< can_reach_with_e[n->states[i]].size()<<endl;
        // for(it = can_reach_with_e[n->states[i]].begin(); it !=can_reach_with_e[n->states[i]].end(); ++it){
        //     cout<<*it <<" ";
        // }
        // cout<<endl;
    }

    for(int i = 0; i < n->states.size(); i++){
        State* st = ind_to_state[n->states[i]];
        st->transitions['E'].clear();
        set<int> can_reach = can_reach_with_e[n->states[i]];
        for(it2 = can_reach.begin(); it2 != can_reach.end(); ++it2){
            if(n->accept_states.find(*it2) != n->accept_states.end()){
                n->accept_states.insert(n->states[i]);
            }
            State* can_reach_state = ind_to_state[*it2];
            for(const auto& [key, value] : can_reach_state->transitions){
                if(key == 'E') continue;
                for(it = value.begin(); it != value.end(); ++it){
                    st->transitions[key].insert(*it);
                }
            }
        }
    }
}

void create_one_start(NFA* n){
    if(n->accept_states.size() == 1) return;
    State* new_start_state = new State;
    new_start_state->id = 0;
    n->states.insert(n->states.begin(), 0);
    ind_to_state[0] = new_start_state;
    for(it = n->start_states.begin(); it != n->start_states.end(); ++it){
        new_start_state->transitions['E'].insert(*it);
    }
    n->start_states.clear();
    n->start_states.insert(0);
}   

string regex_to_nfa(string regex){
    NFA* res = get_nfa(regex);
    create_one_start(res);
    // cout<<"states: " << res->accept_states.size()<<endl;
    deleteE(res);

    return nfa_to_string(res);
}

string replace_with_e(string regex){
    string input_string = regex;
    string target_substring = "()";
    string replacement_string = "E";
    
    size_t pos = input_string.find(target_substring);
    while (pos != string::npos) {
        input_string.replace(pos, target_substring.length(), replacement_string);
        pos = input_string.find(target_substring, pos + replacement_string.length());
    }
    
    // std::cout << input_string << std::endl;
    return input_string;
}

void reset(){
    ind_to_state.clear();
}

int cnt = 0;

void writeInFile(string st){
    string temp = to_string(cnt);
    if(temp.length() == 1) temp = "0" + temp;
    string filename = "out" + temp + ".txt";
    string path = "D:\\kai semestri\\vsCode\\c++\\Public tests\\P1\\res" + string(1, PATH_SEPARATOR); // replace with the actual path to the directory
    ofstream outfile(path + filename, ofstream::binary); // create a new file in the specified directory
    if (outfile.is_open()) {
        outfile << st << endl;
        outfile.close();
    } else {
        cout << "Unable to open file." << endl;
    }
    cnt++;
}


int main() {
    // while(true){
        state_index = 1;
        string regex ;//"(a?b*?c?(0|1)*)*"  (ab*c(0|1)*)*
        cin>>regex;
        regex = replace_with_e(regex);
        regex = add_concat(regex);
        string postfix = to_postfix(regex);
        // cout<<"postfix : "<< postfix<<endl;
        string res = regex_to_nfa(postfix);
        cout<<res;
    // }

    // test();
    return 0;
}  