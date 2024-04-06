This program simulates a temperature monitoring system using multiple sensors. Each sensor records a series of temperatures, which are then compiled into a report. The report analyzes the temperature data to identify significant temperature variations and provides insights into the recorded temperatures.

Features
Random Temperature Generation: Each sensor generates 60 random temperature readings ranging from -100.0 to 70.0 degrees. This simulation uses a thread-safe random number generator to ensure that multiple threads can generate numbers concurrently without any conflict.

Concurrent Data Collection: The program uses multiple threads, each responsible for simulating a sensor and recording its temperature readings. This simulates a real-world scenario where multiple sensors might operate simultaneously.

Data Analysis: After collecting all temperature readings, the program analyzes the data to find:

The five highest and lowest temperatures recorded across all sensors.
The ten-minute interval (among all sensors) with the largest temperature difference, indicating a significant change in the environment.
Implementation Details
The program defines two main classes: Sensor and Report.

Sensor: Responsible for storing and recording temperature readings. Each instance simulates a single sensor.

Report: Manages a collection of sensors and compiles the temperature report. It initiates the recording process for all sensors concurrently and analyzes the collected data.

The temperature recording process is thread-safe, with a mutex protecting the random number generator, ensuring that concurrent accesses do not result in data races.

Once all sensors have recorded their temperatures, the program compiles a comprehensive report. This report includes sorting all temperatures to identify the extreme values and iterating through the sorted temperatures to find the largest difference within any ten consecutive readings.
