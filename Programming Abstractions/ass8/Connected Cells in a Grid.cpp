#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);
vector<string> split(const string &);

/*
 * Complete the 'connectedCell' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts 2D_INTEGER_ARRAY matrix as parameter.
 */
bool inBounds(int row, int col, int matRow, int matCol){
    return row >= 0 && row < matRow && col >= 0 && col < matCol;
}

int connectedCell(vector<vector<int>> matrix) {
    int res = 0;
    int cur = 0;
    int visited[matrix.size()][matrix[0].size()];
    for(int i =0; i<  matrix.size(); i++){
        for(int j =0; j< matrix[0].size(); j++){
            visited[i][j] = 0;
        }
    }
    int dx[8] = {0, 1, 1, 1, 0, -1, -1, -1};
    int dy[8] = {1, 1, 0, -1, -1, -1, 0, 1};
    for(int i = 0; i < matrix.size(); i++){
        for(int j = 0; j < matrix[0].size(); j++){
            int curNum = matrix[i][j];         
            if(curNum == 1 && !(visited[i][j] == 1)){
                pair<int,int> coordinate = make_pair(i, j);   
                queue<pair<int,int>> q;
                q.push(coordinate);
                visited[i][j] = 1; 
                while(!q.empty()){
                    pair<int,int> curCor = q.front();
                    cout<<curCor.first<< " " << curCor.second<<endl;
                    q.pop();
                    cur++;                   
                    for(int k = 0; k < 8; k++){
                        int nextRow = curCor.first + dy[k];
                        int nextCol = curCor.second + dx[k]; 
                        if(!(visited[nextRow][nextCol] == 1) &&  inBounds(nextRow, nextCol,                                matrix.size(), matrix[0].size()) && matrix[nextRow][nextCol] == 1){
                             visited[nextRow][nextCol] = 1; 
                            q.push(make_pair(nextRow, nextCol));
                        }
                    }
                }
            }
            if(cur > res) res = cur;
            cur = 0;
        }
    }
    return res;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    string m_temp;
    getline(cin, m_temp);

    int m = stoi(ltrim(rtrim(m_temp)));

    vector<vector<int>> matrix(n);

    for (int i = 0; i < n; i++) {
        matrix[i].resize(m);

        string matrix_row_temp_temp;
        getline(cin, matrix_row_temp_temp);

        vector<string> matrix_row_temp = split(rtrim(matrix_row_temp_temp));

        for (int j = 0; j < m; j++) {
            int matrix_row_item = stoi(matrix_row_temp[j]);

            matrix[i][j] = matrix_row_item;
        }
    }

    int result = connectedCell(matrix);

    fout << result << "\n";

    fout.close();

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
