
/*

1) GraphAI -> display after clik

2)id in class, ArrayList[]

3)testBoot

*/ 




float SIZE = 5;            //размер клетки в пикселях

float speedThread = 0;     //скорость симуляции в отдельном потоке
float speedRigor  = 0.1;   //скорость симуляции за счёт качества

float grassSpeed = 3 ;     //скорость роста травы
float grassMaxHP = 5;     //максимальное hp травы
float grassStartHP = 3;   //

float bootHanger = 1;     //количество hp, которое теряет бот каждый ход
float bootMaxCapture = 3; //Количество еды, которое бот может сьесть за раз

boolean worldXopen = false;//боты могут переходить через верхий край в нижний, и наоборот
boolean worldYopen = false;//боты могут переходить через правый край в левый , и наоборот

int kolType = 6; //Количество видов ботов
color[] colorType = { //цвета ботов
color(100), color(0,0,255), color(255), color(0,255,0), color(255,0,0), color(255,0,255), color(0,255,255)
};

void setup()
{
  speedRigor = min(1,max(0,speedRigor));
  
  size(displayWidth,displayHeight);
  
  strokeWeight(0);
  for(int i=0; i < kolType; i++) kolBoot[i] = new ArrayList<PVector>();
  generateWorld(0,0,displayWidth,displayHeight-25);

}

boolean pause = false;
int tablo = 0;
void draw()
{
  if(pause)return;
  if(tablo==0)graph();
  else if(tablo==1)tablo1(0,0,width,height);
  else if(tablo==2)tablo2();

  if(!world.active)
  display();
  
  if(boot.size()>0)boot.get(0).graphAI();
  
}


void keyPressed()
{
  if(key == CODED){
    if(keyCode == UP){
      if(!pause)
      threadStart();
      else pause = false;
    }
    else if(keyCode == DOWN){
      if(world.active)
      threadStop();
      else pause = true;
    }
    else if(keyCode == RIGHT){
      tablo = (tablo+1) % 3;
      if(tablo == 0){
        reclear();
      }
    
      if(pause)
      if(tablo==0)graph();
      else if(tablo==1)tablo1(0,0,width,height);
      else if(tablo==2)tablo2();
    }
    else if(keyCode == LEFT){
      tablo = (tablo+2) % 3;  //~ tablo-1
      if(tablo == 0){
        reclear();
      }
    
      if(pause)
      if(tablo==0)graph();
      else if(tablo==1)tablo1(0,0,width,height);
      else if(tablo==2)tablo2();
    }
  }
  else if(key == '1'){
    spawnBoot("BootMy",10,100);
  }
  else if(key == '2'){
    spawnBoot("BootDNA",10,100);
  }
  else if(key == '3'){
    spawnBoot("BootN",10,100);
  }
  else if(key == '4'){
    spawnBoot("BootN2",10,100);
  }
  else if(key == '5'){
    spawnBoot("BootLord",10,100);
  }
  else if(key == '0'){
    spawnBoot("BootXyn",10,100);
  }
  else if(key  == ' '){
    threadStop();
    generateWorld(0,0,displayWidth,displayHeight-25);
    
    if(pause)
    if(tablo==0)graph();
    else if(tablo==1)tablo1(0,0,width,height);
  }
  
}

Boot botGraphAi;
void mousePressed(){
  if(tablo==0 && pause){
    
     for(Boot b : boot){
       if(mouseX>b.x*SIZE+worldX && mouseX<(b.x+1)*SIZE+worldX &&
       mouseY>b.y*SIZE+worldY && mouseY<(b.y+1)*SIZE+worldY){
         tablo = 4;
         botGraphAi = b;
         GraphAI(b);
       }
     }
  }
  //if(tablo==4)display(b);
}

void reclear(){
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      SOIL[i][j].active=true;
    }
  }
}

void threadStart(){
  if(world.active)return;
  world.active = true;
  world.start();
}
void threadStop()
{
  if(!world.active)return;
  world.active=false;
  try {
  world.join();
  } catch (InterruptedException e) {
  e.printStackTrace();
  }
  world = new World();
  //reclear();
}

World world  = new World();
class World extends Thread
{
  int time;
  boolean active = false;
  
  void run()
  {
    while(active)
    {
      if(speedThread==0){
        display();
        continue;
      }
      time = second() + minute()*60;
      display();
      time = second() + minute()*60 - time;
      try{
        this.sleep(max(int(1000/60/speedThread)-time,0));
      }
      catch(InterruptedException e){
        println("la-la-la");
      }
    }
  }
  
}
