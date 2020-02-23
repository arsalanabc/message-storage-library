# JAVA Message storage library

Write a small library for message storage. The library needs to track messages by both sender and ID. The sender is an arbitrary string, and the ID is a type and format of your choosing.

### Your library needs to expose the following functions:

    addMessage(sender: String, body: String) -> id: TypeOfYourChoice: Saves a received message.
    readMessage(id: TypeOfYourChoice) -> body: String: Returns the contents of the message.
    findMessages(sender: String) -> Collection<id>: Returns message ids that match this sender. The collection can be any array, list, iterable, etc.

It should have the following properties:

###    Durability: After the execution of any of the above functions, stopping the process and restarting - or having the process crash - should not lose data.
    Low memory footprint: Bodies of messages should not be held in memory for longer than needed to respond to callers.

### Example run:

    id1 = addMessage("bob", "Hello")
    id2 = addMessage("alice", "Test")
    readMessage(id2)  # Should return "Test"

#### Restart the program

    id3 = addMessage("bob", "Hello")
    findMessages("bob")  # Should return {id1, id3}, not necessarily in that order
    readMessage(id1)  # Should return "Hello"

Choose the language and toolset of your choice. You are free to use any internet resource for language and library reference.
