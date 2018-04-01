// Slow Jigler (Mac OS X)
// Include the mouse library
#include <DigiMouse.h>

int sleep_seconds = 55;

void setup() {
  // Enable mouse features
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
  DigiMouse.moveX(-1); // Move left 1 pixel
  sleep();
  DigiMouse.moveX(-1); // Move left 1 pixel
  sleep();
  DigiMouse.moveX(4);  // Move right 4 pixels
  sleep();
}
