# Sparta-Simulator

## Table of Contents:

> * [Summary](#summary)
> * [Pom Dependencies](#pom-dependencies)
> * [Properties file & Connecting to localhost](#properties-file--connecting-to-localhost)
> * [How to use the program](#how-to-use-the-program)
> * [Creation phases](#creation-phases)
> * [Testing file reader & SQL data transfer](#testing-file-reader--sql-data-transfer)
> * [Logging with Log4j](#logging-with-log4j)
> * [Project Management: Scrum](#project-management-scrum)

## Summary
>This project is a Sparta simulator that checks the general performance of the company over different months under random circumstances. 
The simulator generates a random number of *trainees, Sparta academy centres and clients* over a period of time chosen by the user in months. 
Depending on how the company is doing, *centres* might close and *clients* might not require company services anymore.
However, new *centres* are open every two months and new *clients* can contract Sparta's services.


## Pom Dependencies
> The dependencies used for this project are:
>> * [org.junit.jupiter:5.8.2](https://junit.org/junit5/) - For testing.
>> * [org.apache.logging.log4j:2.17.1](https://logging.apache.org/log4j/2.x/) - For logging.
>> * [mysql:8.0.25](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-installing-maven.html) - For database connection and update.

## MySQL Set up
> The approach for this project was to follow the Data Access Object 
([DAO](https://www.oracle.com/java/technologies/dataaccessobject.html)) 
design pattern to abstract and encapsulate all access to the data source.
This design pattern manages the connection with the data source to obtain and store data.
It implements the access mechanism required to work with the data source.
> 
> *Factory for Data Access Objects Strategy:*
<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/153708510-bd39862b-3fc2-4c08-8252-ab804d9d527e.png"/> </p>



> In order to have access to the database, the database has to be created first. 
> For simplicity, it has to be created first in MySQL as shown:
>```sql
>CREATE DATABASE IF NOT EXISTS trainees;
>USE trainees;
>```

## Properties file & Connecting to localhost

> The mySQL dependency is used with all its associated methods to work on *IntelliJ*:
>```xml
><dependency>
>    <groupId>mysql</groupId>
>    <artifactId>mysql-connector-java</artifactId>
>    <version>8.0.25</version>
></dependency>
>```

>The *TraineeDAO's openConnection()* method creates the connection between the IntelliJ and MySQL after 
> passing the information from the *mysql.properties* file:
>```properties
>dburl=jdbc:mysql://localhost:3306/trainees
>dbuserid=root
>dbpassword=[MYSQL_SERVER_PASSWORD]
>```
>Once the connection is set, the program will work on the *TraineeDAO* instance created during the simulation. This will allow performing actions through the methods provided in that Class such as retrieving information or updating the database.

## How to use the program
> * Project download & set-up
> 
> The first thing the user has to do is download the compressed project on GitHub as shown:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154701030-2057f34b-aef4-46f6-9556-cc89218d7037.png"/> </p>

> Once is in the local machine, it has to be extracted to be used:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154701127-fb301512-b1e6-4bcf-bbca-bc3aa6a05e11.png"/> </p>

> Then, the user has to open the file in their local program. In this case, it's being used *IntelliJ* to run the program:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/153816556-dcbdc43f-7075-47c3-9a91-d1379a2afe16.png"/> </p>

> After that, the user must find the project folder in the right directory so, it can be loaded:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154334431-25306a8b-27c7-45cd-be32-9390f868d04b.png"/> </p>

> * Using the program
> 
> To start the simulation, the *Main* must be run. Then, the program will ask the user how long they want the simulation to be run:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154674673-d9c45a80-2a86-439a-b910-48710fcce22e.png"/> </p>

> The second question will be how the user wants the information to be displayed, monthly or at the end of the simulation:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154339732-9bd89e8c-6752-4ffb-bf6c-a43340d7b883.png"/> </p>

> This should be an example of the information displayed breakdown by type of course. 
> This should include *number of centres open, closed and full* and *trainees being trained and waiting to be trained*:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154677735-1488274e-24b1-4f80-b0cf-d2e9053f6b6f.png"/> </p>

## Creation phases

> This project goes through 3 phases:
> * Phase 1 - Setting-up the simulation:
>>  * The simulation will ask for how long the user wants to run it.
>>  * A random number of trainees are generated **every month** and, they are distributed among the different centres depending on their capacity. If there is no room for them in any centre, they will be moved to a waiting list and following the **[FIFO](https://en.wikipedia.org/wiki/FIFO_and_LIFO_accounting)** system.
>>  * A new training centre is open **every two months** and can train up to **100 trainees**.
> * Phase 2 - Adding new centres and type of courses:
>>  * The simulation should now offer the choice of data at the end of the simulation or a running output updated each month.
>>  * The information displayed is separated by:
>>    * Number of centres open, closed and full (breakdown for each type).
>>    * Number of trainees being trained and waiting to be trained (breakdwon for each type).
>>  * Trainees will be now distributed among different random specialties (**Java, C#, Data, DevOps or Business**).
>>  * There are three types of centres:
>>    * <ins>Training Hub</ins>: Can train up to 100 trainees. One to three centres can be open **every month**.
>>    * <ins>Bootcamp</ins>: Can train up to 500 trainees. This centre can close if attendance is **fewer than 25** trainees after **three months**. Only **two** Bootcamps can be open at a time.
>>    * <ins>Tech Centre</ins>: Can train up to 200 trainees but only teaches one course at a time. The course taught is random selected.
> * Phase 3 - Clients:
>>  * Clients will begin to be randomly created after **one year** of simulation. And they will have a minimum requirement of **15** trainees in one speciality.
>>  * A client will take a random number of trainees trained from one up to full requirement. If the client's needs are satisfied within a year, they will renew the contract, if not they won't do it.
>>  * If a trainee has been in training for **three months**, they are suitable to be placed into a client.
    
#### Testing file reader & SQL data transfer
> Tests covered different areas:
>> * Different user inputs can be handled by the program, including edge cases.
>> * The connections between the program and IntelliJ works as expected and methods create tables, insert, update and retrieve data.
>> * The Trainee Factory create new trainees and retrieve them as expected.
>> * The Trainee class handles their information as expected.

| Test Type       |                                                          Test Results                                                           | Test Coverage                                                                                                                 |
|-----------------|:-------------------------------------------------------------------------------------------------------------------------------:|-------------------------------------------------------------------------------------------------------------------------------|
| Client          |     ![ClientResults](https://user-images.githubusercontent.com/63067669/154776407-8e49e6eb-76a9-4819-9e00-a9a5bf3cc41f.png)     | ![ClientTestCoverage](https://user-images.githubusercontent.com/63067669/154778500-8d6b42de-9249-4a5c-b378-1d7e9b1f6403.png)  |
| Course          |     ![CourseResults](https://user-images.githubusercontent.com/63067669/154776657-901f979a-9971-4967-8f8e-3076cf624f9e.png)     | ![CourseCoverage](https://user-images.githubusercontent.com/63067669/154778496-23df487f-2eb3-4c8e-977d-0df1b0f576ab.png)      |
| Input Parser    |     ![InputParserResults](https://user-images.githubusercontent.com/63067669/154814951-3b745528-a354-4d95-9fcb-ac9a6d712242.png)| ![InputParserCoverage](https://user-images.githubusercontent.com/63067669/154814952-04aad2f8-e365-4e99-a160-031833d81942.png) |
| Trainee DAO     |   ![TraineeDAOResults](https://user-images.githubusercontent.com/63067669/154777416-c954ca94-e2e1-4239-aca9-d7f93f1fa64e.png)   | ![TraineeDAOCoverage](https://user-images.githubusercontent.com/63067669/154778498-da872986-4151-4d7e-a8c1-4f728a77bfea.png)  |
| Trainee Factory | ![TraineeFactoryResults](https://user-images.githubusercontent.com/63067669/154777619-5dfa488b-dd2c-4607-ba92-019b4686a1fc.png) | ![TraineeFactoryCoverage](https://user-images.githubusercontent.com/63067669/154778499-3d4ad999-2e45-468f-9d0b-7a370414498a.png) |
| Trainee         |    ![TraineeResults](https://user-images.githubusercontent.com/63067669/154777839-f2e14bb9-32a6-472a-b7e4-44d87211bece.png)     | ![TraineeCoverage](https://user-images.githubusercontent.com/63067669/154778497-36417ab1-299b-4701-bef0-a4a82f2bfff4.png)     |

## Logging with Log4j
>We can check the program activity through *logging*. This can help the programmers to track the 
steps that the program follows until is completed. Furthermore, this can help to see if there is any potential
issue when it's being run.
> 
>There are two levels covered:
> * Info - It logs when the program has started or finished.
> * Error - It logs when a SQL haven't been able to be executed.

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154814948-0a7ed7b5-403a-4682-b25b-7db2cb1897d8.png"/> </p>

## Project Management: Scrum

> The team was able to track the tasks and requirements assigned to each team member thanks to Trello. 
> These tasks were split into different lists (*Project phases*) and cards (*individual task for each phase*). 
> This way, everyone could know what other members were working on so, they could effectively communicate in the 
> scenario of co-dependent tasks emerged. 
> 
>The team scheduled meetings every day at 10:00, 13:45 and 17:00 to keep updated everyone about individual progress. This also helped to share possible problems about the structure of the project or potential failures. Thanks to these meetings, many issues were solved before they escalated.
>
> The *scrum master* made sure that everyone had a task to fulfill and, alongside with the *Trello manager*, they made sure that the trello board was updated in every session.
>
> This is the image of one of the very first stages of the project:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154815835-e4e568cd-cb7c-45ff-9873-44fe9e116cf1.png"/> </p>

> This image shows all the phases successfully completed at the end of the project:

<p align="center"> <img src="https://user-images.githubusercontent.com/63067669/154815834-4c10f9bd-2671-44f8-b59d-a7cb0b25071a.png"/> </p>
