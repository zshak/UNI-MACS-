    #include <iostream>
    #include <string>  
    #include <vector>
    using namespace std;

    int getResult(vector<int>& tourists, int& fast1, int& fast2, int right){
        if(right == 1) return max(tourists[0],tourists[1]);
        if(right == 2) return tourists[0] + tourists[1] + tourists[2];

        //1 
        int case1 = 0;
        case1+=fast1 + 2*fast2 + max(tourists[right],tourists[right - 1]);

        //2
        int case2 = 0;
        case2 += 2*fast1 + tourists[right] + tourists[right - 1];

        return min(case1,case2) + getResult(tourists, fast1, fast2, right - 2);
    }

    int main(){

        int n;
        cin>>n;

        int fast1;
        int fast2;

        vector<int> tourists;

        for(int i = 0; i < n; i++){
            int speed;
            cin>>speed;
            if(i == 0) fast1 = speed;
            if(i == 1) fast2 = speed;
            tourists.push_back(speed);
        }

        if(n == 1){
            cout<<tourists[0]<<endl;
        }else{
            cout<<getResult(tourists, fast1, fast2,n - 1)<<endl;
        }

        return 0;
    }