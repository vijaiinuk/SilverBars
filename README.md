# SilverBars
REST app for Order management

- This is an app to manage (Create/View/Cancel) new orders.  Also this App can list out positions and display a live board of consolidated view of orders, grouped by OrderType (BUY/SELL)
- Have used the Spring Boot to create REST end points for these operations on the Order management
- Once the successful maven build/install, run the below command (from the project/current dir of this README file), to start the application
	java -jar target\gs-rest-service-0.1.0.jar
- After successful start of the application, you can view the REST endpoints via the below Swagger documentation
	http://localhost:8080/swagger-ui.html#/order-controller
