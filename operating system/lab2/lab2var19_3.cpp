#include<iostream> 
#include<cstdlib> 
#include<vector>
#include <windows.h>
#include <string>
#include <fstream>
#include <stdlib.h>
#include <time.h>
#include <future>
#include <map>
#include<mutex> 
#include<thread> 
#include <chrono>
#include <queue>
using std::chrono::duration_cast;
using std::chrono::microseconds;
using std::chrono::steady_clock;
using namespace std;

int TaskCount = 0;
int ThreadCount = 0;
int id = 1;
static exception_ptr eptr = nullptr;
mutex read;
mutex close;
ofstream fout("Bout.txt");
struct ThreadInfo {
    int id;
    int taskAmount;
    double time;
    ThreadInfo() : id(0), taskAmount(0), time(0) {}
    void print() {
        cout << "Information about thread " << id << endl
            << "Amount of tasks: " << taskAmount << endl
            << "Time: " << time << endl;
    }
};
struct Statistics {
    int taskAmount;
    int error;
    int noSolutions;
    double minTime;
    double maxTime;
    vector<ThreadInfo> threads;
    int timeWriteInFile;
    Statistics() : taskAmount(0), error(0), noSolutions(0), timeWriteInFile(0), minTime(INT_MAX), maxTime(0) {}
    void print() {
        cout << "Amount of solved tasks: " << taskAmount << endl
            << "Amount of error-solved: " << error << endl
            << "Amount of solved without solutions: " << noSolutions << endl
            << "Max time of solution: " << maxTime << endl
            << "Min time of solution: " << minTime << endl
            << "Information about threads: " << endl;
        for (int i = 0; i < threads.size(); i++) {
            threads.at(i).print();
            cout << endl;
        }
        cout << "Time of writing in file: " << timeWriteInFile << endl;
    }
};

Statistics statist;

class Polynom {

    int exponent;
    std::vector<long long> coeff;
    int deleter;
    bool solved;

public:

    Polynom() : exponent(0), deleter(0), coeff(NULL), solved(false) {}
    bool getStatus() { return solved; }
    void setStatus(bool st) { solved = st; }
    int getExp() { return exponent; }
    std::vector<long long> getCoeff() { return coeff; }
    int getDeleter() { return deleter; }
    void setExponent(int exp) { exponent = exp; }
    void setCoeff(std::vector<long long> v) {
        if (coeff.size() > 0) coeff.clear();
        coeff = v;
    }
    void setDeleter(int delet) { deleter = delet; }
    void printTask() {
        fout << "Step: " << exponent << std::endl
            << "Coefficients" << std::endl;
        for (auto elem : coeff) fout << elem << " ";
        fout << std::endl
            << "Deleter: x-(" << deleter << ")" << std::endl
            << std::endl;
    }
    void printSolution() {
        fout << "Step: " << exponent << std::endl
            << "Coefficients" << std::endl;
        for (auto elem : coeff) fout << elem << " ";
        fout << std::endl
            << "Ostatok: " << deleter << std::endl
            << std::endl;
    }
};

queue<Polynom> tasks;

void generateTasks() {

    fout << "Tasks" << endl;
    int ttask = TaskCount;
    if (TaskCount >= 3) {
        ttask -= 2;
        Polynom one;
        one.setExponent(1);
        std::vector<long long> tmp;
        tmp.push_back(1);
        int c = rand() % 20;
        tmp.push_back(c);
        one.setDeleter((-1) * c);
        one.setCoeff(tmp);
        Polynom nosol;
        nosol.setExponent(0);
        nosol.setDeleter(0);
        tasks.push(one);
        tasks.push(nosol);
    }
    for (int i = 0; i < ttask; i++) {
        Polynom task;
        std::vector<long long> temp;
        task.setExponent(rand() % 50);
        for (int j = 0; j <= task.getExp(); j++) {
            temp.push_back(rand() % 500 - 250);
        }
        task.setCoeff(temp);
        task.setDeleter(rand() % 500 - 250);
        tasks.push(task);
    }
}

struct Solution {

    Polynom task;
    Polynom result;
    Solution() {}
    void printSolution() {
        fout << "task:" << endl;
        task.printTask();
        fout << "Solution" << endl;
        result.printSolution();
    }
    void printTaskWithoutSolution() {
        fout << "task:" << endl;
        task.printTask();
        fout << "No solution or error-solved task" << endl << endl;
    }
};

vector<Solution> solutions;
int currThread = 0, closeThread = 0;
int ctask = 0;

void closeMyThreads(thread* threadss) {
    std::lock_guard<std::mutex> l(close);
    closeThread = closeThread / 2;
        for (int i = 0; i < closeThread; i++) {
            if (threadss[i].joinable()) threadss[i].join();
        }
}
bool flag = false;

void solutionThread(thread* threadss) {

    std::lock_guard<std::mutex> lock(read);
    double time = 0;
    int count = 0;
    ThreadInfo info;
    info.id = id++;
    int cnst;
    if (info.id == ThreadCount - 1) cnst = tasks.size();
    else cnst = TaskCount / ThreadCount + 1;
    if (tasks.empty()) {
        info.time = 0;
        info.taskAmount = 0;
    }
    for (; !tasks.empty(); count++) {
       if (ctask == TaskCount / 2) {
           closeThread = currThread;
           cout << 3 << endl;
           thread mth(closeMyThreads, threadss);
           if (mth.joinable()) mth.join();
           flag = true;
           cout << 4785685468 << endl;
           ctask += TaskCount * 4 / 2;
       }
        ctask++;
        steady_clock::time_point start = steady_clock::now(); // start
        Solution sl;
        if (count < cnst) {
            Polynom temp = tasks.front();
            tasks.pop();

            if (temp.getExp() < 1) {
                sl.task.setStatus(false);
                solutions.push_back(sl);
                statist.noSolutions += 1;
                statist.taskAmount += 1;
                info.taskAmount += 1;
            }
            else {
                bool err = false;
                try {
                    std::vector<int> gorner;
                    gorner.push_back(temp.getCoeff().at(0));
                    for (int i = 1; i <= temp.getExp(); i++) {
                        gorner.push_back(temp.getDeleter() * gorner.at(i - 1) + temp.getCoeff().at(i));
                    }
                    Polynom result;
                    result.setExponent(temp.getExp() - 1);
                    result.setDeleter(gorner.at(gorner.size() - 1));
                    std::vector<long long> tmp;
                    tmp.assign(gorner.begin(), gorner.end() - 1);
                    result.setStatus(true);
                    result.setCoeff(tmp);
                    sl.task = temp;
                    sl.result = result;
                    sl.task.setStatus(true);
                    solutions.push_back(sl);
                }
                catch (...) {
                    err = true;
                }
                if (err == true) {
                    temp.setStatus(false);
                    statist.error += 1;
                }
                info.taskAmount += 1;
                statist.taskAmount += 1;

            }
            steady_clock::time_point end = steady_clock::now(); // end
            double ti = duration_cast<microseconds>(end - start).count();
            time = time + ti;
            if (ti < statist.minTime) statist.minTime = ti;
            if (ti > statist.maxTime) statist.maxTime = ti;
            info.time = time;
        }
        else {
            steady_clock::time_point end = steady_clock::now(); // end
            double ti = duration_cast<microseconds>(end - start).count();
            time = time + ti;
            if (ti < statist.minTime) statist.minTime = ti;
            if (ti > statist.maxTime) statist.maxTime = ti;
            info.time = time;
            statist.threads.push_back(info);
            return;
        }
    }
    statist.threads.push_back(info);
}

void generateThreads() {

    std::vector<Polynom> temp;
    std::vector<Polynom> res;
    int count = 0;


    std::thread* threads = new std::thread[ThreadCount];
    for (int i = 0; i < ThreadCount; i++) {
        currThread = i;
        threads[i] = std::thread(solutionThread, threads);
    }

    for (int i = closeThread + 1; i < ThreadCount; i++) {
        if (threads[i].joinable()) {
            threads[i].join();
        }
    }
}

void printSolutionsFile() {
    steady_clock::time_point start = steady_clock::now(); // start
    for (int i = 0; i < solutions.size(); i++) {
        fout << i + 1 << " ";
        if (solutions.at(i).task.getStatus() == true) {
            solutions.at(i).printSolution();
        }
        else {
            solutions.at(i).printTaskWithoutSolution();
        }
    }
    steady_clock::time_point end = steady_clock::now(); // end
    statist.timeWriteInFile = duration_cast<microseconds>(end - start).count();
}

int main() {
    setlocale(LC_ALL, "1251");
    srand(time(NULL));
    // 1
    int tc, taskc;
    cout << "Enter ThreadCount TaskCount" << endl;
    cin >> tc >> taskc;
    ThreadCount = tc;
    TaskCount = taskc;
    if (ThreadCount == 0) statist.minTime = 0;
    // 2
    generateTasks();
    // 3
    generateThreads();
    // 4
    printSolutionsFile();
    // 5
    statist.print();

    return 0;
}