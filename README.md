# os-process

Utility classes to run OS process

* 

## How to add it into your app

### Maven


```xml
<repositories>
    <repository>
        <id>pne</id>
        <name>payneteasy repo</name>
        <url>https://maven.pne.io</url>
    </repository>
</repositories>
  
<dependency>
    <groupId>com.payneteasy.os-process</groupId>
    <artifactId>os-process-impl</artifactId>
    <version>1.0-3</version>
</dependency>
```

### Example

```java
IProcessService processService = new ProcessServiceImpl(executor);
ProcessRunResult result = processService.runProcess(new ProcessDescriptor(
    execFile.getAbsolutePath()
    , Arrays.asList(jsonFile.getAbsolutePath(), aFile.getAbsolutePath())
    , Collections.emptyList()
    , execFile.getParentFile()
));

if (result.getExitCode() != 0) {
    throw new IllegalStateException(format("Cannot create maxmind db file: [exit code = %d, output = %s]"
       , result.getExitCode(), result.getOutput()));
}
```

