#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);
vector<string> split(const string &);

/*
 * Complete the 'printShortestPath' function below.
 *
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER i_start
 *  3. INTEGER j_start
 *  4. INTEGER i_end
 *  5. INTEGER j_end
 */
 bool inBounds(int row, int col, int n){
     return row>=0 && row<n && col >=0 && col < n;
 }

//UL, UR, R, LR, LL, L
string getDir(int y, int x){
    if(y ==  -2 && x == -1) return "UL";
    if(y == -2 && x == 1) return "UR";
    if(y == 0 && x == 2) return "R";
    if(y == 2 && x == 1) return "LR";
    if(y == 2 && x == -1) return "LL";
    return "L";
}

void printVector (vector<string>& path){
    for(int i = 0; i < path.size(); i++){
        cout<<path[i]<< " ";
    }
    cout<<endl;
}

void printShortestPath(int n, int i_start, int j_start, int i_end, int j_end) {
    int dx[6] = {-1, 1, 2, 1, -1, -2};
    int dy[6] = {-2, -2, 0, 2, 2, 0};
    int visited[n][n];
    for(int i = 0; i< n; i++){
        for(int j = 0; j <n; j++){
            visited[i][j] = 0;
        }
    }
    map<pair<int,int>, vector<string>> paths;
    pair<int,int> endCor = make_pair(i_end, j_end);
    queue<pair<int,int>> q;
    q.push(make_pair(i_start, j_start));
    visited[i_start][j_start] = 1;
    while(!q.empty()){
        pair<int,int> cur = q.front();
        q.pop();
        if(cur == endCor){
            cout<<paths[cur].size()<<endl;
            printVector(paths[cur]);
            return;
        }
        for(int i = 0; i < 6; i++){
            int nextRow = cur.first + dy[i];
            int nextCol = cur.second + dx[i];
            if(inBounds(nextRow,nextCol, n) && !(visited[nextRow][nextCol] == 1)){
                visited[nextRow][nextCol] = 1;
                string dir = getDir(dy[i], dx[i]);
                paths[make_pair(nextRow, nextCol)] = paths[cur];
                paths[make_pair(nextRow, nextCol)].push_back(dir);
                q.push(make_pair(nextRow, nextCol));
            }
        }
    }
    cout<<"Impossible";
}

int main()
{
    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    string first_multiple_input_temp;
    getline(cin, first_multiple_input_temp);

    vector<string> first_multiple_input = split(rtrim(first_multiple_input_temp));

    int i_start = stoi(first_multiple_input[0]);

    int j_start = stoi(first_multiple_input[1]);

    int i_end = stoi(first_multiple_input[2]);

    int j_end = stoi(first_multiple_input[3]);

    printShortestPath(n, i_start, j_start, i_end, j_end);

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

vector<string> split(const string &str) {
    vector<string> tokens;

    string::size_type start = 0;
    string::size_type end = 0;

    while ((end = str.find(" ", start)) != string::npos) {
        tokens.push_back(str.substr(start, end - start));

        start = end + 1;
    }

    tokens.push_back(str.substr(start));

    return tokens;
}
