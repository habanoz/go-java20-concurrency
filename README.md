# Implementing Go Concurrency Code in Java20
Java implementation of Go Tour Concurrency Examples

Most of the go code is simply copied from [Official Go Tour Page](https://go.dev/tour/concurrency/1)
Only exercise codes are added.

Java implementations are based on **Java 20**.

## Approach

Java codes are tried to be as close as to the Go codes. When applicable similar features are used. When a similar feature is absent, it is tried to be devised using builtin java constructs.

## Virtual Threads

Virtual threads are only available for preview in Java 20. So it is required to enable preview while buiding and running the code files.

## Source Files Index

| Example                           | GO                                                | Java                                                  |
|-----------------------------------|---------------------------------------------------|-------------------------------------------------------|
| Goroutines                        | [link](go-concurrency/goroutines.go)              | [link](java20-concurrency/Goroutines.go)              |
| Channels                          | [link](go-concurrency/channels.go)                | [link](java20-concurrency/Channels.java)              |
| Buffered Channels                 | [link](go-concurrency/buffered_channels.go)       | [link](java20-concurrency/BufferedChannels.java)      |
| Range and Close                   | [link](go-concurrency/range_and_close.go)         | [link](java20-concurrency/RandAndClose.java)          |
| Select                            | [link](go-concurrency/select.go)                  | [link](java20-concurrency/Selectjava)                 |
| Default Selection                 | [link](go-concurrency/default_selection.go)       | [link](java20-concurrency/DefaultSelection.java)      |
| Exercise: Equivalent Binary Trees | [link](go-concurrency/equivalent_binary_trees.go) | [link](java20-concurrency/EquivalentBinaryTrees.java) |
| sync.Mutex                        | [link](go-concurrency/sync_mutex.go)              | [link](java20-concurrency/SyncMutex.java)             |
| Exercise: Web Crawler             | [link](go-concurrency/web_crawler.go)             | [link](java20-concurrency/WebCrawler.java)            |
