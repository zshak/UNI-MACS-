#include <bits/stdc++.h>
using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'separateNumbers' function below.
 *
 * The function accepts STRING s as parameter.
 */

bool isSeparable(string input, string soFar){
    if(input.length() == 0) return true;
    if(input[0] == '0') return false;
    for(int i = 0; i < input.length(); i++){
        if(atol(input.substr(0,i+1).c_str()) == atol(soFar.c_str()) + 1){
            if(isSeparable(input.substr(i+1), input.substr(0,i+1))) return true;
        }
    }
    return false;
}

void separateNumbers(string input){
    if(input[0] == '0') {
        cout<<"NO"<<endl;
        return;
    }
    for(int i = 0; i < input.length() - 1; i++){
        string substr = input.substr(0, i+1);
        if(isSeparable(input.substr(i+1), substr)){
            cout<<"YES "<<substr<<endl;
            return;
        }
    }
    cout<<"NO"<<endl;
}

int main()
{
    string q_temp;
    getline(cin, q_temp);

    int q = stoi(ltrim(rtrim(q_temp)));

    for (int q_itr = 0; q_itr < q; q_itr++) {
        string s;
        getline(cin, s);

        separateNumbers(s);
    }

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}
