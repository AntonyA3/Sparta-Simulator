# Sparta-Simulator

## Table of Contents:

* [Summary](#summary)
* [Pom Dependencies](#pom-dependencies)
* [Properties file & Connecting to localhost](#properties-file--connecting-to-localhost)
* [How to use the program](#how-to-use-the-program)
* [Creation phases](#creation-phases)
* [Testing file reader & SQL data transfer](#testing-file-reader--sql-data-transfer)
* [Logging with Log4j](#logging-with-log4j)
* [Project Management: Scrum](#project-management-scrum)

### Sections
#### Summary
This project is a Sparta simulator that checks the general performance of the company over different months under random circumstances. 
The simulator generates a random number of *trainees, Sparta academy centres and clients* over a period of time chosen by the user in months. 
Depending on how the company is doing, *centres* might close and *clients* might not require company services anymore.
However, new *centres* are open every two months and new *clients* can contract Sparta's services.
#### Pom Dependencies
The dependencies used for this project are:
* org.junit.jupiter:5.8.2 - For testing.
* org.apache.logging.log4j:2.17.1 - For logging.
* mysql:8.0.25 - For database connection and update.
#### MySQL Set up
The approach for this project was to follow the Data Access Object ([DAO](https://www.oracle.com/java/technologies/dataaccessobject.html)) design pattern to abstract and encapsulate all access to the data source. This design pattern manages the connection with the data source to obtain and store data. It implements the access mechanism required to work with the data source.

![DAOPattern](https://user-images.githubusercontent.com/63067669/153708510-bd39862b-3fc2-4c08-8252-ab804d9d527e.png)  
*Factory for Data Access Objects Strategy*

In order to have access to the database, the database has to be created first. For simplicity, it has to be created first in MySQL as shown:

```sql
CREATE DATABASE IF NOT EXISTS trainees;
USE trainees;
```
#### Properties file & Connecting to localhost

The mySQL dependency is used with all its associated methods to work on *IntelliJ*:

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
```

The *TraineeDAO's openConnection()* method creates the connection between the IntelliJ and MySQL after passing the information from the *mysql.properties* file:

````properties
dburl=jdbc:mysql://localhost:3306/trainees
dbuserid=root
dbpassword=[MYSQL_SERVER_PASSWORD]
````

Once the connection is set, the program will work on the *TraineeDAO* instance created during the simulation. This will allow performing actions through the methods provided in that Class such as retrieving information or updating the database.

#### How to use the program
* Project download & set-up

The first thing the user has to do is download the compressed project on GitHub as shown:

![howtouse1](https://user-images.githubusercontent.com/63067669/154333191-fa3512c7-066e-4cf0-ad7d-44d4fda11d28.png)

Once is in the local machine, it has to be extracted to be used:

![howtouse2](https://user-images.githubusercontent.com/63067669/154333384-35635ef5-a15b-49ea-8f27-e338ba578555.png)

Then, the user has to open the file in their local program. In this case, it's being used *IntelliJ* to run the program:

![HowToUse3](https://user-images.githubusercontent.com/63067669/153816556-dcbdc43f-7075-47c3-9a91-d1379a2afe16.png)

After that, the user must find the project folder in the right directory so, it can be loaded:

![howtouse4](https://user-images.githubusercontent.com/63067669/154334431-25306a8b-27c7-45cd-be32-9390f868d04b.png)

* Using the program
To start the simulation, the *Main* must be run. Then, the program will ask the user how long they want the simulation to be run:

![](MISSING TO SHOW THE MIN-MAX MONTHS RANGE SHOWN ON THE TERMINAL)

The second question will be how the user wants the information to be displayed, monthly or at the end of the simulation:

![howtouse6](https://user-images.githubusercontent.com/63067669/154339732-9bd89e8c-6752-4ffb-bf6c-a43340d7b883.png)

This should an example of the information displayed breakdown by type of course. This information should include *number of centres open, closed and full* and *trainees being trained and waiting to be trained*:

![](IMAGE OF THE RESULTS DISPLAYED)


#### Creation phases

This project goes through 3 phases:

* Phase 1 - Setting-up the simulation
  * The simulation will ask for how long the user wants to run it.
  * A random number of trainees are generated **every month** and, they are distributed among the different centres depending on their capacity. If there is no room for them in any centre, they will be moved to a waiting list and following the **[FIFO](https://en.wikipedia.org/wiki/FIFO_and_LIFO_accounting)** system.
  * A new training centre is open **every two months** and can train up to **100 trainees**.

* Phase 2 - Adding new centres and type of courses
  * The simulation should now offer the choice of data at the end of the simulation or a running output updated each month.
  * The information displayed is separated by:
    * Number of centres open, closed and full (breakdown for each type).
    * Number of trainees being trained and waiting to be trained (breakdwon for each type).
  * Trainees will be now distributed among different random specialties (**Java, C#, Data, DevOps or Business**).
  * There are three types of centres:
    * <ins>Training Hub</ins>: Can train up to 100 trainees. One to three centres can be open **every month**.
    * <ins>Bootcamp</ins>: Can train up to 500 trainees. This centre can close if attendance is **fewer than 25** trainees after **three months**. Only **two** Bootcamps can be open at a time.
    * <ins>Tech Centre</ins>: Can train up to 200 trainees but only teaches one course at a time. The course taught is random selected.

* Phase 3 - Clients
  * Clients will begin to be randomly created after **one year** of simulation. And they will have a minimum requirement of **15** trainees in one speciality.
  * A client will take a random number of trainees trained from one up to full requirement. If the client's needs are satisfied within a year, they will renew the contract, if not they won't do it.
  * If a trainee has been in training for **three months**, they are suitable to be placed into a client.
    
#### Testing file reader & SQL data transfer.
Tests covered different areas:
* Different user inputs can be handled by the program, including edge cases.
* The simulation works as expected and handles the information according to the requirements.
* The connections between the program and IntelliJ works as expected and methods create tables, insert, update and retrieve data.
* The Trainee Factory create new trainees and retrieve them as expected.
* The Trainee class handles their information as expected.

| Test Type       |                                                       Test Results                                                       |
|-----------------|:------------------------------------------------------------------------------------------------------------------------:|
| Input Parser    |                                                          ![]()                                                           |
| Simulation      |                                                          ![]()                                                           |
| Trainee DAO     |                                                          ![]()                                                           |
| Trainee Factory |                                                          ![]()                                                           |
| Trainee Test    |                                                          ![]()                                                           |

#### Logging with Log4j
We can check the program activity through *logging*. This can help the programmers to track the steps that the program follows until is completed. Furthermore, this can help to see if there is any potential issue when it's being run.

![]()

#### Project Management: Scrum


