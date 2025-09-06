#include <iostream>
    #include <string> 
#include <vector>
#include <map>
using namespace std;
 
char multiple_choice[4]; 
 
//hint taken from : https://codeforces.com/gym/103860/attachments/download/17128/CCPC-Finals-2021-Tutorial.pdf
 
void solve(){
 
    
}
 
void init(){
    for(int i = 0; i < 4; i++){
        multiple_choice[i] = (char)('A' + i);
    }
}
 
void get_scores_for_right(int l, int num_tries,vector<string>& tries,vector<int>& marks,
            vector<int>& scores,int & result, map<vector<int>,int>& num_of_scores){
    if(l == 10){
        vector<int> scores_to_check(num_tries);
        scores_to_check.reserve(num_tries);
        for(int i = 0; i < num_tries; i++){
            scores_to_check[i] = marks[i] - scores[i];
        }
 
        result+=num_of_scores[scores_to_check];
        return;
    }
 
     for(int i = 0; i < 4; i++){
        char choice = multiple_choice[i];
        for(int i = 0; i < num_tries; i++){
            if(tries[i][l] == choice) scores[i]++;
        }
 
        get_scores_for_right(l + 1, num_tries, tries, marks, scores, result, num_of_scores);
 
        for(int i = 0; i < num_tries; i++){
            if(tries[i][l] == choice) scores[i]--;
        }
    }
}
 
void get_scores_for_left(int l, int num_tries,vector<string>& tries,vector<int>& marks,
            vector<int>& scores,int & result, map<vector<int>,int>& num_of_scores){
    if(l == 5){
        num_of_scores[scores]++;
        return;
    }
 
    for(int i = 0; i < 4; i++){
        char choice = multiple_choice[i];
        for(int i = 0; i < num_tries; i++){
            if(tries[i][l] == choice) scores[i]++;
        }
 
        get_scores_for_left(l + 1, num_tries, tries, marks, scores, result, num_of_scores);
 
        for(int i = 0; i < num_tries; i++){
            if(tries[i][l] == choice) scores[i]--;
        }
    }
}
 
int main()
{
    init();
    int num_tests;
    cin>>num_tests;
    for(int i = 0; i < num_tests; i++){
        int num_tries;
        cin>>num_tries;
        vector<string> tries(num_tries);
        tries.reserve(num_tries);
 
        vector<int> marks(num_tries);
        marks.reserve(num_tries);
 
        for(int i = 0; i < num_tries; i++){
            cin>>tries[i]>>marks[i];
            marks[i] = marks[i] / 10;
        }
 
        vector<int> scores(num_tries);
        scores.reserve(num_tries);
        for(int i = 0; i < num_tries; i++){
            scores[i] = 0;
        }
        int result = 0;
        map<vector<int>, int> num_of_scores;
 
        get_scores_for_left(0, num_tries, tries, marks, scores, result, num_of_scores);
        get_scores_for_right(5, num_tries, tries, marks, scores, result, num_of_scores);
        cout<<result<<endl;
    }
 
    return 0;
}
