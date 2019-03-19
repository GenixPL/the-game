#How to start program
###### Option 1:
1. install [gradle](https://spring.io/guides/gs/gradle/)
2. open terminal
3. `gradlew run --args="[args]"`
   
`example: gradlew run --args="2019-01-pl --address 127.0.0.1 --port 2001 --conf C:\Users\genix\Desktop\conf.json"`
    
###### Option 2: (IntelliJ)
1. create new multiple run configurations
2. check "Allow parallel run" in each of them
2. select Main class as starting point in each of them
3. add arguments in each of them
  `(opened ports are specified in cs' console, they grow by one starting from speficied in cs/gm args)`