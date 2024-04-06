#include <algorithm>
#include <chrono>
#include <iostream>
#include <mutex>
#include <random>
#include <thread>
#include <vector>

std::mutex mtx;
std::default_random_engine generator(time(0)); std::uniform_real_distribution<double> distribution(-100.0, 70.0);
double randomNumber() {
  std::lock_guard<std::mutex> lock(mtx);
  return distribution(generator);
}

class Sensor {
public:
  std::vector<double> temps;
  Sensor() : temps(60) {}
  void recordTemperature(int i) { temps[i] = randomNumber(); }
  void recordTemperatures() {
    for (int i = 0; i < 60; ++i) {
      this->recordTemperature(i);
    }
  }
};

class Report {
public:
  std::vector<Sensor> sensors;
  Report(int n) : sensors(n) {}
  void compileReport() {
    std::vector<std::thread> threads;
    for (auto &sensor : sensors)
      threads.emplace_back(&Sensor::recordTemperatures, &sensor);
    for (auto &thread : threads)
      thread.join();

    std::vector<double> allTemps;
    for (auto &sensor : sensors)
      allTemps.insert(allTemps.end(), sensor.temps.begin(), sensor.temps.end());

    std::sort(allTemps.begin(), allTemps.end());

    std::cout << "5 Lowest temperatures: ";
    for (int i = 0; i < 5; i++) {
      std::cout << allTemps[i] << ", ";
    }
    std::cout << "\n";
    std::cout << "5 Highest temperatures: ";
    for (int i = allTemps.size() - 5; i < allTemps.size(); i++) {
      std::cout << allTemps[i] << ", ";
    }

    int maxIndex = 0;
    double maxDiff = 0.0;
    for (int i = 0; i < 50; i++) {
      double minTemp =
          *std::min_element(allTemps.begin() + i, allTemps.begin() + i + 10);
      double maxTemp =
          *std::max_element(allTemps.begin() + i, allTemps.begin() + i + 10);
      if (maxTemp - minTemp > maxDiff) {
        maxDiff = maxTemp - minTemp;
        maxIndex = i;
      }
    }
    std::cout << "The largest temperature difference was observed during "
              << maxIndex << " to " << maxIndex + 10
              << " interval. The difference was " << maxDiff << "\n";
  }
};

int main() {
  Report report(8);
  report.compileReport();
  return 0;
}