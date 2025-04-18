# SC-Trade Service Documentation

## Overview
The `sc-trade` service is a Java application designed to enrich trade data. It reads trade data files and uses a static product data file to translate `productId` into `productName`, while performing data validation and processing.

## Features
- **API Interface**: Provides an API to enrich trade data.
- **Data Translation**: Translates `productId` to `productName`.
- **Data Validation**:
  - Ensures that the `date` is a valid `yyyyMMdd` format date, otherwise discards the row and logs an error.
  - If the product name is unavailable, logs the missing mapping and sets the product name to `Missing Product Name`.
- **Performance Handling**: Capable of processing very large trade datasets (millions of records) and large product datasets (10k to 100k records).

## Technology Stack
- **Spring Boot**: 3.2.1
- **Java**: 17
- **Maven**: For project management and dependency management
- **Hutool**: For CSV file processing and date validation

## How to Run the Service

1. **Clone the Project**:
   ```bash
   git clone https://github.com/wzk9261/sc-trade.git
   cd sc-trade
   ```


2. **Build the Project**:
   ```bash
   mvn clean install
   ```


3. **Run the Service**:
   ```bash
   mvn spring-boot:run
   ```


## How to Use the API

### Request Example
Use the `curl` command to send a POST request:
```bash
curl --location 'localhost:8080/api/v1/enrich' \
--form 'file=@"/Users/yourself/IdeaProjects/sc-trade/target/test-classes/trade.csv"'
```


### Request Parameters
- **File**: `trade.csv` file containing trade data.

### Response Example
```csv
date,productName,currency,price
20160101,Treasury Bills Domestic,EUR,10
20160101,Corporate Bonds Domestic,EUR,20.1
20160101,REPO Domestic,EUR,30.34
20160101,Missing Product Name,EUR,35.34
```

## Code Structure

- **`src/main/java/com/sc/trade/model`**:
  - `Product.java`: Product model class.
- **`src/main/java/com/sc/trade/service`**:
  - `EnrichmentService.java`: Service class for processing trade data enrichment.
- **`src/main/java/com/sc/trade/repo`**:
  - `ProductRepository.java`: Product data repository class, responsible for loading and querying product data.
- **`src/main/java/com/sc/trade/util`**:
  - `TradeDateUtil.java`: Date validation utility class.
- **`src/main/java/com/sc/trade/constant`**:
  - `CsvHeaderConstant.java`: CSV header constants.
- **`src/main/java/com/sc/trade/controller`**:
  - `EnrichController.java`: Controller class providing the enrich API.
- **`src/main/resources`**:
  - `application.yml`: Application configuration file.
  - `application-large-csv.yml`: Configuration file for handling large CSV files.
  - `product.csv`: Sample product data file.
- **`src/test/java/com/sc/trade/service`**:
  - `EnrichmentServiceTest.java`: Unit tests for the `EnrichmentService` class, verifying the processing of trade data.
  - `LargeCsvTest.java`: Tests for generating large CSV files for performance testing.
- **`src/test/java/com/sc/trade/util`**:
  - `TradeDateUtilTest.java`: Unit tests for the `TradeDateUtil` class, ensuring date validation correctness.
- **`src/test/java/com/sc/trade/controller`**:
  - `EnrichControllerTest.java`: Unit tests for the `EnrichController` class, verifying API endpoint behavior and response handling.
- **`src/test/resources`**:
  - `trade.csv`: Sample trade data file.

## Limitations
- The service does not depend on any external caching or other external services.
- The service loads product data into memory at startup, using a `HashMap` for fast queries. For very large product datasets, optimization of data loading and querying mechanisms may be necessary.

## Design Discussion
- **Data Loading**: Product data is loaded into memory when the application starts, using a `HashMap` for quick access.
- **Data Processing**: The Hutool library is used for CSV file reading and writing operations to ensure efficient handling of large datasets.
- **Thread Safety**: The `productMap` in `ProductRepository` is thread-safe when reading, ensuring correct access in a multi-threaded environment.

## Performance Analysis

### Data Preparation

Below analysis is based on the large CSV files: large_product.csv with 1 million rows and large_trade.csv with 2 million rows, which can be generated by LargeCsvTest.java.

Below is how we call by Postman with both large request and response file.

![Postman_High_Performance](img/Postman_High_Performance.png)

### Analysis By JFR

We focus on the memory usage of a period of JFR, the record starts from the startup of Spring Boot Application, and ends at time when the first enrich API call is over. So the first peaks are for the startup of Spring Boot Application, and second peaks are for enrich API call. The peak usage of the memory is 1.28 GB, as we can watch from the last green dot in the below image.

![JFR_Timeline_Memory.png](img/JFR_Timeline_Memory.png)

Then we go through the CPU time of each steps in the main handling method: process in EnrichmentService. As we can watch from the screenshot below, the log.error at line 41 costs most of the CPU time. Noted: you can read the yellow words in the screenshots.

![Java_Code_Time_Cost](img/Java_Code_Time_Cost.png)

Same for the memory analysis.

![Java_Code_Time_Cost](img/Java_Code_Time_Cost.png)

### CPU and Memory Usage during a few Calls

![CPU_Heap_Memory_Cost_Timeline](img/CPU_Heap_Memory_Cost_Timeline.png)

## Improvement Suggestions

- **Caching Mechanism**: Introduce a caching mechanism (like Redis) to keep product data query efficiency and meanwhile store the large dataset.
- **Batch Processing**: For very large trade datasets, consider batch processing to reduce memory usage.
- **Logging**: Add more logging for debugging and monitoring.
- **Unit Tests**: Increase unit tests to improve code coverage and stability.
