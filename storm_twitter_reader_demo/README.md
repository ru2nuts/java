Simple Storm Trident topology, that reads filtered Twitter data, performs bot filtering and prints formatted messages.

To run - clone the code and run:

[mvn test]

The test will start up local Storm, connect to Twitter API, run for 30 sec and then shut down. You can see all the received (and not-filtered out messages) in the console, they start with ">>>".
