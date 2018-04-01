// Slow Jigler (Mac OS X)
// Include the mouse library
#include <DigiMouse.h>

int sleep_seconds = 55;

void setup() {
  // Enable mouse feautures
  DigiMouse.begin();
  // Wait before starting the program
  DigiMouse.delay(1000);
}

void sleep(){
  int i;
  for (i = 0; i < sleep_seconds; i++)
    DigiMouse.delay(1000); // Sleep 1 second
}

void loop() {
  // The main Jiggle code
  DigiMouse.moveX(-8);  // Move left 8 pixels
  sleep();
  DigiMouse.moveX(8);   // Move right 8 pixels
  sleep();
}
