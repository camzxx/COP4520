Servant Present Management System
Overview
This program simulates a system where multiple servants (threads) manage a collection of presents. The presents are initially stored in a stack (bagOfPresents) and are managed through a linked list that represents a chain of presents waiting to be processed. Each present is identified by a unique tag number.

Features
Present Addition: Servants can take a present from the bag and add it to the chain. This operation places the present in the correct position in the chain based on its tag number to keep the chain sorted.

Thank You Note Writing and Present Removal: A servant can write a thank you note for the first present in the chain and then remove it from the chain.

Present Search: Servants can search for a present in the chain by its tag number. This simulates the process of checking if a specific present is still in the chain.

Implementation Details
The program uses the std::stack to store presents in the bag. Each present is identified by a unique giftTag.

The presents in the chain are managed using a custom linked list. Each node in the list represents a present and includes a giftTag and a pointer to the next present in the chain.

To ensure thread safety when multiple servants access and modify the chain concurrently, each node in the list is protected by a std::mutex. This prevents data races and ensures that the linked list maintains integrity even in a multi-threaded environment.

The main logic of the program is encapsulated in the servantWork function, which is executed by each thread. Servants randomly choose between adding a present to the chain, removing a present from the chain, or searching for a present in the chain.

