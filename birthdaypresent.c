#include <algorithm>
#include <iostream>
#include <mutex>
#include <random>
#include <stack>
#include <thread>
#include <vector>

using namespace std;

class Node {
public:
  int giftTag;
  Node *next;
  mutex nodeLock;
};

class LinkedList {
public:
  Node *dummyNode = new Node();
  LinkedList() { dummyNode->next = nullptr; }
  void addNode(int giftTag) {
    Node *temp = new Node();
    temp->giftTag = giftTag;
    temp->next = nullptr;
    Node *current = dummyNode;
    current->nodeLock.lock();
    while (current->next && current->next->giftTag < giftTag) {
      Node *nextNode = current->next;
      nextNode->nodeLock.lock();
      current->nodeLock.unlock();
      current = nextNode;
    }
    temp->next = current->next;
    current->next = temp;
    current->nodeLock.unlock();
  }
  void removeNode() {
    if (dummyNode->next) {
      Node *temp = dummyNode->next;
      temp->nodeLock.lock();
      dummyNode->next = temp->next;
      temp->nodeLock.unlock();
      delete temp;
    }
  }
};

stack<int> bagOfPresents;
LinkedList list;

void servantWork(int id) {
  
  while (!bagOfPresents.empty() || list.dummyNode->next) {
    int task = std::rand() % 3; 

    switch (task) {
    case 0: 
      if (!bagOfPresents.empty()) {
        int tag = bagOfPresents.top();
        bagOfPresents.pop();
        list.addNode(tag);
        cout << "Servant " << id << " added present with tag " << tag
             << " to chain." << endl;
      }
      break;
    case 1: 
      if (list.dummyNode->next) {
        cout
            << "Servant " << id
            << " is writing a thank you note and removing a present from chain."
            << endl;
        list.removeNode();
      }
      break;
    case 2: 
      if (list.dummyNode->next) {
        int searchTag =
            rand() % 500000 +
            1; 
        Node *temp = list.dummyNode->next;
        while (temp) {
          temp->nodeLock.lock();
          if (temp->giftTag == searchTag) {
            temp->nodeLock.unlock();
            cout << "Servant " << id << " found gift with tag " << searchTag
                 << endl;
            break;
          }
          Node *nextNode = temp->next;
          temp->nodeLock.unlock();
          temp = nextNode;
        }
        if (!temp) 
          cout << "Servant " << id << " couldn't find gift with tag "
               << searchTag << endl;
      }
      break;
    }
  }
  cout << "Servant " << id << " is done." << endl;
}

int main() {
  thread threads[4];
  for (int i = 1; i <= 500000; ++i) {
    bagOfPresents.push(i);
  }
  for (int i = 0; i < 4; ++i) {
    threads[i] = thread(servantWork, i + 1);
  }
  for (auto &th : threads)
    th.join();
  return 0;
}
