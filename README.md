# System Overview

The primary purpose of the developed system is to manage the operations of a pharmaceutical warehouse. The user categories of the system are:

### Warehouse Department Employees

These users can manage all operations related to the drugs distributed in the warehouse (Add, Update, Delete, Search).

### Sales Department Employees

These users manage the orders.

### Accounting Department Employees

These users can only manage payments related to orders. They also have access to statistical data regarding the movement of drugs.

## System Architecture

The system is based on a three-tier architecture:

1. **Data Layer**: Organization and storage of the data managed by the system. A MySQL Relational Database was developed.
2. **Business Logic Layer**: Functions executed by the system were defined and implemented. Java programming language was used.
3. **Presentation Layer**: The system interfaces were specified and developed in Java.

## Database

The database was developed using MySQL Database Management System. The tables developed in the database are shown below:

- **Systemuser**: Includes information about system users.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/61e94e47-253b-4550-a54e-eeb9db296795)
- **Supplier**: Includes information about the company's suppliers.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/16b59ad4-771f-4206-947c-ae841396d947)
- **Salesman**: Includes information about the company's salesmen.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/bc1e4174-45ef-4c52-ba66-d1a764b1b93c)
- **Producer**: Includes information about drug producers.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/e7ece7fc-52bf-40c7-bf68-5491781fd99d)
- **Payment**: Includes information about customer payments for orders.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/536f64b1-76cd-4b7f-b3b5-27e083e24e4d)
- **Orderitems**: Contains data about the contents of an order.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/589bf3b9-fa7a-4927-804c-018b1f516d18)
- **Dragtype**: Includes drug categories.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/16324226-0fc0-47df-a354-afb7f85441f0)
- **Dragpart**: Includes information about batches of stored drugs.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/622365b8-6d4b-427f-8490-bcac3c47178f)
- **Dragorder**: Includes information about the orders received by the company.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/77b32cd8-e901-433a-8203-f7536cb14f1a)
- **Drag**: Includes information about stored products.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/97d876bf-440f-4984-8a86-5984d93525dc)
- **Department**: Includes information about the company's departments.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/7946bf3c-9006-4d6d-aaa4-5d29eb02c035)
- **Customer**: Includes information about the company's customers.
  ![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/49f9f539-148d-4ac8-a572-a5408c3c4bc0)
- **City**: Includes information about cities.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/65d5f81a-dd5d-4b22-aea9-a3a44f876091)

### Database Relationships Diagram

![Database Relationships](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/443ef485-4b1f-4fd7-8e71-de54ed53e6d4)


## Operations

The business operations were designed using an object-oriented approach and implemented in Java. The implementation is based on the JPA (Java Persistence API) standard, which provides a programming interface for communication between Java class objects and the database. For each table in the database, an object class was developed. These classes are used to implement the system's functionality and include mechanisms for database communication. Management of these objects is done by classes implementing an additional layer, providing interfaces for database operations. These classes include the suffix `JpaController`. Where necessary, sub-classes with the suffix `Handler` were developed to extend Controller mechanisms.

## Interfaces and Usage

### Login

Upon starting the application, the user must enter a username and password to log in.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/d75b4b87-bb1c-4d2b-97e4-d8088e02df8f)

### Home Screen

After successful login, the corresponding options for each user category are displayed.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/89350f87-3014-40bb-8091-48c6ea0ece65)

#### Warehouse Department Options

On this screen, users can set search criteria for the products managed by the company. By selecting the criteria and clicking SEARCH, the products that meet these criteria are displayed in the first table.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/39cfa728-5b08-4f4d-b0fa-55b29ebaf2f9)

Clicking on a product displays its batches in the lower table. If batch availability is low, a popup window appears.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/615f802f-fbbe-4ebe-a55f-264bbafe9110)

Users can update product details by selecting the product and clicking UPDATE. They can also delete a product by clicking DELETE or add a new product by clicking ADD, entering the details in the provided form. Batch management allows adding new batches with [+], removing with [-], and updating batch details, all confirmed by clicking UPDATE.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/bac2712e-3b7b-4699-ac45-07505a088a81)

#### Sales Department Options

Users can set search criteria for orders.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/bd21eea6-f48b-4849-9c84-1bd1cc6547e4)
Clicking SEARCH displays the orders that meet the criteria. Selecting an order and clicking GO navigates to the order details screen. 
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/cfcb0bff-2a22-4737-a9cb-42f7e90d9326)

Users can modify all order details except payments (handled by the Accounting Department), calculate the final order amount with BILLING, and issue an invoice with PRINT.
#### Accounting Department Options
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/a6542a4e-2b1e-415d-a3eb-be93f3ddb1dc)


##### Statistics

Users can view statistical data by clicking STATISTICS on the home screen, select the desired time frame, and click the corresponding button.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/a3511f0c-80e8-468c-8274-59760e1a4d89)

##### Payments

Clicking PAYMENTS navigates to the payment management screen.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/50ad7a85-968a-44c5-87a0-b2727975bc92)

Users can search for an order using the drop-down menu, view payments, add new payments, update payment amounts, or delete payments.
![image](https://github.com/Vaggelisvl/Pharmaceutical-Warehouse-Management-System/assets/73244655/5a09df4d-93ed-4bc8-ab7c-18d22ff476bb)
