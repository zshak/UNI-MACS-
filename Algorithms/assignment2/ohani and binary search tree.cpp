#include <bits/stdc++.h>

using namespace std;

//https://www.geeksforgeeks.org/longest-monotonically-increasing-subsequence-size-n-log-n/ 
//longest increasing subsequence 
//c++(gcc 8.3)

struct node {
    int value;
    node* left;
    node* right;
};

int CeilIndex(vector<int> &v, int l, int r, int key)
{
    while (r - l > 1)
    {
        int m = l + (r - l) / 2;
        if (v[m] >= key)
            r = m;
        else
            l = m;
    }

    return r;
}

int LongestIncreasingSubsequenceLength(vector<int>& v, int size)
{
    if (size == 0)
        return 0;

    vector<int> tail(size, 0);
    int length = 1;

    tail[0] = v[0];
    for (size_t i = 1; i < size; i++)
    {

        if (v[i] < tail[0])
            tail[0] = v[i];

        else if (v[i] > tail[length - 1])
            tail[length++] = v[i];

        else
            tail[CeilIndex(tail, -1, length - 1, v[i])] = v[i];
    }

    return length;
}

node* buildTree(int size){
    node* root = new node;
    root->left = NULL;
    root->right = NULL;
    if(size == 1) return root;
    queue<node*> q;
    q.push(root);
    size--;
    while(true){
        node* cur = q.front();
        q.pop();
        node* left = new node;
        left->left = NULL;
        left->right = NULL;

        node* right = new node;
        right->left = NULL;
        right->right = NULL;

        cur->left = left;
        q.push(left);
        size--;
        if(size == 0) break;
        cur->right = right;
        q.push(right);
        size--;
        if(size == 0) break;
    }
    return root;
}

void fillTree(node* root, vector<int>& elems, int& ind){
    if(!root) return;
    fillTree(root->left, elems, ind);
    root->value = elems[ind++];
    fillTree(root->right, elems, ind);
}

void fillAnswers(node* root, vector<int>& answers){
    if(!root) return;
    if(root->left) {
        answers[(root->left)->value] = root->value;
        fillAnswers(root->left,answers);
    }
    if(root->right) {
        answers[(root->right)->value] = root->value;
        fillAnswers(root->right, answers);
    }
}

void getParents(vector<int>& elems, vector<int>& answers){
    node* root = buildTree(elems.size());
    int ind = 0;
    fillTree(root, elems, ind);
    answers[root->value] = -1;
    fillAnswers(root, answers);
}

int main()
{
    int testCases;
    cin >> testCases;

    for (int testcase = 0; testcase < testCases; testcase++)
    {
        unordered_set<int> rame;
        int numElems;
        cin >> numElems;
        vector<int> elems(numElems);
        elems.reserve(numElems);
        for (int i = 0; i < numElems; i++)
        {
            int a;
            cin>>a;
            elems[i] = a;
        }

        cout<<"Case "<<testcase + 1<<":"<<endl;
        cout<<"Minimum Move: "<<numElems - LongestIncreasingSubsequenceLength(elems, numElems)<<endl;

        sort(elems.begin(), elems.end());
        

        //unordered_map<int,int> answers;
        vector<int> answers(numElems);
        answers.reserve(numElems);
        getParents(elems, answers);

        for(int i = 0; i < numElems - 1; i++){
            cout<<answers[elems[i]] << " ";
        }
        cout<<answers[elems[numElems - 1]]<<endl;
    }

    return 0;
}