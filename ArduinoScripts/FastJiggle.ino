// FastJiggle

// Include the mouse library
#include <DigiMouse.h>

int sleep_seconds = 0;

void setup() {
  pinMode(1, OUTPUT);
  // Enable mouse feautures
  DigiMouse.begin();
  // Wait before starting the program
  DigiMouse.delay(1000);
}

void sleep() {
  int i;
  for (i = 0; i < sleep_seconds; i++)
    DigiMouse.delay(1000); // Sleep 1 second
}

void moveXY(int x, int y) {
  if (x != 0)
    DigiMouse.moveX(x);
  if (y != 0)
    DigiMouse.moveY(y);
  sleep();
  DigiMouse.delay(50);
  DigiMouse.update();
}

void loop() {
  // The main Jiggle code
  moveXY(-10,0); //1
  moveXY(-10,0);
  moveXY(0,10);
  moveXY(0,10);
  moveXY(0,10);  //5
  moveXY(0,10);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(10,0);  //10
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(10,0);  //15
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,10);
  moveXY(0,10);
  moveXY(-10,0); //20
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,10);  //25
  moveXY(0,10);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,-10);
  moveXY(0,-10); //30
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,10);
  moveXY(0,10);
  moveXY(0,10);  //35
  moveXY(0,10);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,-10); //40
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(-10,0);
  moveXY(-10,0); //45
  moveXY(-10,0);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,10);  //50
  moveXY(0,10);
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(10,0);  //55
  moveXY(10,0);
  moveXY(10,0);
  moveXY(0,10);
  moveXY(0,10);
  moveXY(-10,0); //60
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10); //65
  moveXY(0,-10);
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(-10,0); //70
  moveXY(0,10);
  moveXY(0,10);
  moveXY(10,0);
  moveXY(10,0);
  moveXY(10,0);  //75
  moveXY(0,10);
  moveXY(0,10);
  moveXY(-10,0);
  moveXY(-10,0);
  moveXY(-10,0); //80
  moveXY(-10,0);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10);
  moveXY(0,-10); //85
  moveXY(-10,0); //86
}
