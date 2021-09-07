import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.lang.reflect.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BootNeurons extends PApplet {


/*

1) GraphAI -> display after clik

2)id in class, ArrayList[]

3)testBoot

*/ 




float SIZE = 5;            //размер клетки в пикселях

float speedThread = 0;     //скорость симуляции в отдельном потоке
float speedRigor  = 0.1f;   //скорость симуляции за счёт качества

float grassSpeed = 3 ;     //скорость роста травы
float grassMaxHP = 5;     //максимальное hp травы
float grassStartHP = 3;   //

float bootHanger = 1;     //количество hp, которое теряет бот каждый ход
float bootMaxCapture = 3; //Количество еды, которое бот может сьесть за раз

boolean worldXopen = false;//боты могут переходить через верхий край в нижний, и наоборот
boolean worldYopen = false;//боты могут переходить через правый край в левый , и наоборот

int kolType = 6; //Количество видов ботов
int[] colorType = { //цвета ботов
color(100), color(0,0,255), color(255), color(0,255,0), color(255,0,0), color(255,0,255), color(0,255,255)
};

public void setup()
{
  speedRigor = min(1,max(0,speedRigor));
  
  
  
  strokeWeight(0);
  for(int i=0; i < kolType; i++) kolBoot[i] = new ArrayList<PVector>();
  generateWorld(0,0,displayWidth,displayHeight-25);

}

boolean pause = false;
int tablo = 0;
public void draw()
{
  if(pause)return;
  if(tablo==0)graph();
  else if(tablo==1)tablo1(0,0,width,height);
  else if(tablo==2)tablo2();

  if(!world.active)
  display();
  
  if(boot.size()>0)boot.get(0).graphAI();
  
}


public void keyPressed()
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
public void mousePressed(){
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

public void reclear(){
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      SOIL[i][j].active=true;
    }
  }
}

public void threadStart(){
  if(world.active)return;
  world.active = true;
  world.start();
}
public void threadStop()
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
  
  public void run()
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
        this.sleep(max(PApplet.parseInt(1000/60/speedThread)-time,0));
      }
      catch(InterruptedException e){
        println("la-la-la");
      }
    }
  }
  
}
abstract class Boot
{
  
  int x,y;
  int direction=0;
  
  float hp;
  
  public abstract void teem();  //размножение клеток
  public abstract void AI();    //интелект, решает куда ити и когда размножаться
  public abstract void graphAI(); //"изображает свой интелект"
  
  public abstract int getID();//каждый вид ботов имеет свой id

  public Boot(){
    kolB[getID()]++;
    kolB[0]++;
  }
  
  public void setCord(int x, int y)
  {
    this.x=x;
    this.y=y;
  }
  
  public void setHp(float hp){
    this.hp=hp;
  }
 
  public Soil getSoil(int x, int y)
  {
    direction%=4;
    int a;
    switch(direction)
    {
      case 0:
        break;
      case 1:
        a = x;
        x =-y;
        y = a;
        break;
      case 2:
        x =-x;
        y =-y;
        break;
      case 3:
        a = x;
        x = y;
        y =-a;
        break;
    }
    
    x+=this.x;
    y+=this.y;
    
    if(worldXopen){
      if(x>=worldWidth) x-=worldWidth;
      else if(x<0)      x+=worldWidth;
    }
    else{
      if(x>=worldWidth) x=this.x;
      else if(x<0)      x=this.x;
    }
    if(worldYopen){
      if(y>=worldHeight) y-=worldHeight;
      else if(y<0)       y+=worldHeight; 
    }
    else{
      if(y>=worldHeight) y=this.y;
      else if(y<0)       y=this.y;
    }

    
    
    return SOIL[x][y];
  }
  public void setDirect(int x, int y)
  {
    //direction = 0;
    if(this.y-y<0)direction=1; //1
    if(this.y-y>0)direction=3; //3
    if(this.x-x<0)direction=0; //0
    if(this.x-x>0)direction=2; //2
    
    //   3
    // 2   0
    //   1
    
    if(abs(this.x-x)+abs(this.y-y)>5)direction+=2;
  }
  /*
     Если перейдёт мир с одного края на другой
     (x>worldX...)
  */
  
  //////////////
  
  public void display()
  {
    hp-=bootHanger;
    
    float havk = min(bootMaxCapture,SOIL[x][y].hp);
    hp+=havk;
    SOIL[x][y].hp-=havk;

    if(hp<0){
      death();
    }
    
    SOIL[x][y].active=true;
    AI();
    SOIL[x][y].active=true;
    
  }
  
  public void graph()
  {
    stroke(colorType[getID()]);
    fill(colorType[getID()]);
    rect(x*SIZE,y*SIZE,SIZE,SIZE);
  }
  
  public void death(){
    boot.remove(this);
    kolB[getID()]--;
    kolB[0]--;
    
    SOIL[x][y].hp+=grassMaxHP/10;
  }

}

class BootDNA extends Boot
{
  final static int ID = 1;
  public int getID(){
    return ID;
  }
  
  ArrayList<Integer> DNA = new ArrayList<Integer>();
  int index = 0;
  int iterationMax = 30;
  int valueMax = 50;
  
  public BootDNA(){
    newAI();
  }
  
  BootDNA(BootDNA b)
  {
    hp=b.hp/2;
    //kolBD++;
    
    for(int f : b.DNA)DNA.add(f);
    /*
    if(random(100)<5)DNA.add(int(random(valueMax)));
    if(random(100)<5)DNA.remove(int(random(DNA.size())));*/
    if(random(100)<15)DNA.set(PApplet.parseInt(random(DNA.size())), PApplet.parseInt(random(valueMax/3)));
  }
 
  
  public void AI(){
    AI(0);
    if(random(1000)<1)teem();
  }
  
  public void AI(int iteration){
    
    if(iteration>iterationMax)return;
    
    Soil s = getSoil(0,0); 
    int comand = DNA.get(index);
    
    if(comand==0){
      
      index=(index+1)%DNA.size();
      int hp = DNA.get(index)*5;
      
      if(this.hp>hp)teem();
    }
    else
    if(comand==1){
      
      index=(index+1)%DNA.size();
      int xNew = DNA.get(index) %3 -1;
      
      index=(index+1)%DNA.size();
      int yNew = DNA.get(index) %3 -1;
      
      s = getSoil(xNew,yNew);
    }
    else if(comand==2){
      
      index=(index+1)%DNA.size();
      int xS = DNA.get(index) %3 -1;
      
      index=(index+1)%DNA.size();
      int yS = DNA.get(index) %3 -1;
      
      s = getSoil(xS,yS);
      
      index=(index+1)%DNA.size();
      int hp = DNA.get(index) * PApplet.parseInt(grassMaxHP / valueMax);
      
      if(s.hp>hp)index=(index+1)%DNA.size();
      else index=(index+2)%DNA.size();
      
      AI(iteration+1);
      return;
    }
    else{
      index=(index+comand)%DNA.size();
      AI(iteration+1);
    }
    
    index=(index+1)%DNA.size();
    
    setDirect(s.x,s.y);
    x = s.x;
    y = s.y;
  }
  
  
  public void newAI(){
    for(int i=0; i<valueMax; i++){
      DNA.add(PApplet.parseInt(random(valueMax/10)));
    }
  }
  
  public void graphAI(){
    fill(0);
    stroke(255);
    for(int i = 0; i < DNA.size(); i++){
      rect((i%50)*30,(i-i%50)*30,30,30); 
    }
    
    fill(0,100,0);
    rect((index%50)*30,(index-index%50)*30,30,30);
        
    fill(255);
    stroke(0);
    textSize(20);
    textAlign(CENTER);
    for(int i = 0; i < DNA.size(); i++){
      text(DNA.get(i),(i%50)*30+15,(i-i%50)*30+20); 
    }
    
  }
  
  
  public void teem(){
    BootDNA b = new BootDNA(this);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
    
  }
  /*
  void death(){
    boot.remove(this);
    kolBD--;
    //kolB--;
  }*/
  
}

class BootLord extends Boot{
  
  final static int ID = 5;
  public int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  public BootLord(){
    newAI();
  }

  
  public float sumSoil(Soil s)
  {
    float sumHp = 0;
    for(int i=-1; i<=1; i++){
      for(int j=-1; j<=1; j++){
        sumHp+= s.getSoil(i,j).hp;
      }
    }
    return sumHp;
  }
  
  
  public void AI()
  {  
    net.in(random(1),hp,getSoil(0,0).hp
    ,getSoil(1,0).hp,getSoil(0,1).hp,getSoil(-1,0).hp,getSoil(0,-1).hp
    ,sumSoil( getSoil(2,0) ), sumSoil( getSoil(0,2) ), sumSoil( getSoil(-2,0) ), sumSoil( getSoil(0,-2)) );
    net.display();
    
    Soil s = getSoil(1,0);
    float max=net.out(0);
    
    if(max<net.out(0)){
      max=net.out(0);
      s = getSoil(1,0);
    }
    if(max<net.out(1)){
      max=net.out(1);
      s = getSoil(0,1);
    }
    if(max<net.out(2)){
      max=net.out(2);
      s = getSoil(-1,0);
    }
    if(max<net.out(3)){
      max=net.out(3);
      s = getSoil(0,-1);
    }
    if(max<net.out(4)){
      //hp+=-100;
    }
    if(max<net.out(5)){
      teem();
      //return;
    }

    setDirect(s.x,s.y);
    x = s.x;
    y = s.y;
  }
  
  
  
  public void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,11,10,1);
  }
  public void newAI(NeuralNetwork n)
  {
    net = n.clone();
    if(random(20)<1 )net.changeWeight(1.25f);
    if(random(20)<15)net.changeWeight(0.01f);
  }
  
  public void graphAI(){
    
  }
  
  
  public void teem(){
  if(hp<0)return;
    BootLord b = new BootLord();
    b.setHp(hp/2);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
    b.newAI(net);
  }
  
  /*
  void death(){
    boot.remove(this);
    kolBN2--;
    //kolB--;
  }*/
  
}
class BootMy extends Boot{
  
  final static int ID = 2;
  public int getID(){
    return ID;
  }
  
  float teemHP = 300; //количество hp, после которого клетка делится
  
  public BootMy(){
  }
  public BootMy(float teemHP){
    this.teemHP = teemHP;
  }
  
  
  public void AI(){
    if(kolB[0]-kolB[ID]*2>0)hp+= PApplet.parseFloat(kolB[0]-kolB[ID]*2)/(kolB[0])*bootHanger;
    //if(random(1000)<1)println((kolB[0]-kolB[ID]*2)/(kolB[0])*bootMaxCapture);
    if(kolB[0]-kolB[ID]*2<0)hp+= PApplet.parseFloat(kolB[0]-kolB[ID]*2)/(kolB[0])*bootMaxCapture;
    float max = -1;//getSoil(0,0).hp;
    Soil s = getSoil(0,0);
    
    if(hp>teemHP){
      teem();
      return;
    }
    
    if(max<getSoil(1,0).hp){
      max=getSoil(1,0).hp;
      s = getSoil(1,0);
    }
    if(max<getSoil(-1,0).hp){
      max=getSoil(-1,0).hp;
      s = getSoil(-1,0);
    }
    if(max<getSoil(0,1).hp){
      max=getSoil(0,1).hp;
      s = getSoil(0,1);
    }
    if(max<getSoil(0,-1).hp){
      max=getSoil(0,-1).hp;
      s = getSoil(0,-1);
    }
    
    setDirect(s.x,s.y);
    
    x=s.x;
    y=s.y;
  }
  
  public void graphAI(){
    fill(255);
    textAlign(100);
    text("My Boot", 50,50);
    text("Teem HP = " + teemHP, 50,150);
  }
  
  public void teem(){
    BootMy b = new BootMy(teemHP + random(-1,1));
    b.setHp(hp/2);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
  }
}

class BootN extends Boot{
  
  final static int ID = 3;
  public int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  int RANGE = 1;
  
  public BootN(){
    newAI();
  }
  
  
  public void autoIn(int range)
  {
    ArrayList<Float> ij = new ArrayList<Float>();
    for(int i=-range; i<=range; i++){
      for(int j=-range; j<=range; j++){
        ij.add(getSoil(i,j).hp);
      }
    }
    ij.add(this.hp);
    net.in(ij);
  }
  
  
  
  public void AI()
  {  
    autoIn(RANGE);
    net.display();
    
    Soil s = getSoil(1,0);
    float sum=net.out(0)+net.out(1)+net.out(2)+net.out(3)+net.out(4)+net.out(5);
    float r=random(sum);
    
    //if(max<net.out(0)){
    if(sum-net.out(0)<r && r<sum)
    {
      s = getSoil(1,0);
    }
    sum-=net.out(0);
    
    if(sum-net.out(1)<r && r<sum)
    {
      s = getSoil(0,1);
    }
    sum-=net.out(1);
    
    if(sum-net.out(2)<r && r<sum)
    {
      s = getSoil(-1,0);
    }
    sum-=net.out(2);
    
    if(sum-net.out(3)<r && r<sum)
    {
      s = getSoil(0,-1);
    }
    sum-=net.out(3);
    
    if(sum-net.out(4)<r && r<sum)
    {
      //hp+=-100;
    }
    sum-=net.out(4);
    
    if(sum-net.out(5)<r && r<sum || hp>3000 || random(1000)<1)
    {
      teem();
      //return;
    }

    SOIL[x][y].active=true;
    setDirect(s.x,s.y);
    x = s.x;
    y = s.y;
  }
  
  
  public void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,PApplet.parseInt(sq(RANGE*2+1))+1,100,1);
    
    //↓←↑→
    /*
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,0,0,1,0,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    
    net.addNeuron(0,0,0,0,1,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,0,1,0,0,0,0);
    
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,1,0,0,0,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    
    net.addNeuron(0,0,0,0,0,0,0.01);
    
    net.setLayer();
   */
  }
  public void newAI(NeuralNetwork n)
  {
    net = n.clone();
    if(random(20)<1 )net.changeWeight(1.25f);
    if(random(20)<15)net.changeWeight(0.01f);
  }
  
  public void graphAI(){
    
  }
  
  
  public void teem(){
  if(hp<0)return;
    BootN b = new BootN();
    b.setHp(hp/2);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
    b.newAI(net);
  }
  /*
  void death(){
    boot.remove(this);
    kolBN--;
    //kolB--;
  }*/
  
}

class BootN2 extends Boot{
  
  final static int ID = 4;
  public int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  int RANGE = 1;
  
  public BootN2(){
    newAI();
  }

  
  public void autoIn(int range)
  {
    ArrayList<Float> ij = new ArrayList<Float>();
    for(int i=-range; i<=range; i++){
      for(int j=-range; j<=range; j++){
        ij.add(getSoil(i,j).hp);
      }
    }
    ij.add(this.hp);
    net.in(ij);
  }
  
  
  public void AI()
  {  
    autoIn(RANGE);
    net.display();
    
    Soil s = getSoil(1,0);
    float max=net.out(0);
    
    if(max<net.out(0)){
      max=net.out(0);
      s = getSoil(1,0);
    }
    if(max<net.out(1)){
      max=net.out(1);
      s = getSoil(0,1);
    }
    if(max<net.out(2)){
      max=net.out(2);
      s = getSoil(-1,0);
    }
    if(max<net.out(3)){
      max=net.out(3);
      s = getSoil(0,-1);
    }
    if(max<net.out(4)){
      //hp+=-100;
    }
    if(max<net.out(5) || hp>3000 || random(1000)<1){
      teem();
      //return;
    }

    setDirect(s.x,s.y);
    x = s.x;
    y = s.y;
  }
  
  
  
  public void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,PApplet.parseInt(sq(RANGE*2+1))+1,10,1);
    
    //↓←↑→
    /*
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,0,0,1,0,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    
    net.addNeuron(0,0,0,0,1,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,0,1,0,0,0,0);
    
    net.addNeuron(0,0,0,0,0,0,0);
    net.addNeuron(0,1,0,0,0,0,0);
    net.addNeuron(0,0,0,0,0,0,0);
    
    net.addNeuron(0,0,0,0,0,0,0.01);
    
    net.setLayer();
   */
  }
  public void newAI(NeuralNetwork n)
  {
    net = n.clone();
    if(random(20)<1 )net.changeWeight(1.25f);
    if(random(20)<15)net.changeWeight(0.01f);
  }
  
  public void graphAI(){
    
  }
  
  
  public void teem(){
  if(hp<0)return;
    BootN2 b = new BootN2();
    b.setHp(hp/2);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
    b.newAI(net);
  }
  
  /*
  void death(){
    boot.remove(this);
    kolBN2--;
    //kolB--;
  }*/
  
}
/*
boolean doing;
class Button{
  float x,y;
  float w,h;
  
  String text="";
  float textSize=0;
  
  color fill   = color(200);
  color stroke = color(50);
  
  Button(){}
  
  void setCord(float x, float y, float w, float h){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  void setText(String text, float textSize){
    this.text     = text;
    this.textSize = textSize;
  }
  void setColor(float fill, float stroke){
    this.fill   = color(fill);
    this.stroke = color(stroke);
  }
  void setColor(color fill, color stroke){
    this.fill   = fill;
    this.stroke = stroke;
  }
  
  boolean pressed()
  {
    if(!mousePressed){
      doing = false;
      return false;
    }
    else if(!doing){
      doing = true;
      return true;
    }
    else return false;
  }
  
  void graph()
  {
    fill(fill);
    stroke(stroke);
    rect(x,y,w,h);
    fill(stroke);
    textAlign(CENTER);
    textSize(textSize);
    text(text,x+w/2,y+h/2+textSize/5);
  }
}
*/
public void GraphAI(Boot boot){
  translate(50,50);
  background(0);
  boot.graphAI();
  translate(-50,-50);
}
class Neuron
{
  float deff; //start value
  float in;
  float out;
  ArrayList<Neuron> links = new ArrayList<Neuron>();
  float[] weight;
  
  int id;
  
  Neuron(float d)
  {
    deff = d;
    in   = d;
  }
  Neuron(float d, ArrayList<Neuron> l, float[] w)
  {
    deff   = d;
    in     = d;
    links  = l;
    weight = w;
  }
  
  
  
  public float f2()
  {
         if(in>1)return  1.1f;
    else if(in<1)return  0.9f;
    else         return  1;
  }
  public float f1()
  {
         if(in>0)return  1;
    else if(in<0)return -1;
    else         return  0;
  }
  public float f()
  {
    return 2*atan(in)/PI;
  }
  public float f3()
  {
    return cos(f()*PI);
  }
  public float ReL(){
    return min(0,in);
  }
  
  /////////////
  
  public void display()
  {
    out = in;
    
    for(int i=0; i<links.size(); i++)
    links.get(i).in += out * weight[i];
    
    in = deff;
  }
  public void disp()
  {
    out = in;
    
    for(int i=0; i<links.size(); i++)
    {
      links.get(i).in += out * weight[i];
    }
    
    in = deff;
  }
  
  /////////////
  
  public void changeWeight(float r)
  {
    
    if(r>0.5f)
    {
      deff+=random(-r,r);
      for(int i=0; i<links.size(); i++)
    {
      weight[i]+=random(-r,r);
    }
    }
    
    //deff=100;
    deff+=deff*random(-r,r);
    //if(abs(deff)<0.1)deff+=random(-1,1);
    //if(abs(deff)>500)deff*=0.99;
    for(int i=0; i<links.size(); i++)
    {
      weight[i]+=weight[i]*random(-r,r);
      //weight[i]=random(-0.01,0.01);
      //if(abs(weight[i])<0.01)weight[i]+=random(-0.1,0.1);
      //if(abs(weight[i])>32)weight[i]/=2;
    }
  }
  
}
class Soil
{
  int x,y;
  float hp;
  boolean active = true;
  
  Soil(int x, int y)
  {
    this.x=x;
    this.y=y;
  }
/*
   n
   0     1x
  25     4/3x
  50     2x
  75     4x
  99   100x
  
  (100)/(100-n)=speed
  (1-1/speed)=n
  */
  public void display()
  {
    
    
    if(random(1)<(1-speedRigor)){
      return;
    }
    
    else if(hp<grassMaxHP){
      active = true;
      float sum = getSoil(-1,0).hp + getSoil(1,0).hp + getSoil(0,-1).hp + getSoil(0,1).hp;
      //float sum = 30;
      hp+=sum*0.002f/sqrt(speedRigor)*grassSpeed * 2*y/worldHeight/(hp+1);
      hp=min(hp,grassMaxHP);
    }
  }
  
  public Soil getSoil(int x, int y)
  {
    x+=this.x;
    y+=this.y;
    
    if(worldXopen){
      if(x>=worldWidth) x-=worldWidth;
      else if(x<0)      x+=worldWidth;
    }
    else{
      if(x>=worldWidth) x=this.x;
      else if(x<0)      x=this.x;
    }
    if(worldYopen){
      if(y>=worldHeight) y-=worldHeight;
      else if(y<0)       y+=worldHeight; 
    }
    else{
      if(y>=worldHeight) y=this.y;
      else if(y<0)       y=this.y;
    }
    
    return SOIL[x][y];
  }

  public void graph()
  {
    if(!active)return;
    active = false;
    
    stroke(0,255*hp/grassMaxHP,125*(1-hp/grassMaxHP));
    fill  (0,255*hp/grassMaxHP,125*(1-hp/grassMaxHP));
    rect(x*SIZE,y*SIZE,SIZE,SIZE);
  }
  
}

int age = 0;


ArrayList<PVector>[] kolBoot = new ArrayList[kolType];
int kolB[] = new int[kolType];



ArrayList<PVector> kolGrass   = new ArrayList<PVector>();//история количества травы


int max = 0;  //(для графика)

public void resetStatistic(){
  for(int i=0; i < kolType; i++) kolBoot[i].clear(); 
  age = 0;
  for(int i=0; i < kolType; i++) kolB[i] = 0; 
}


public void adboot(ArrayList<PVector> Int, int kol){
  if(Int.size()>=2){
    if(kol==Int.get(Int.size()-1).y && kol==Int.get(Int.size()-2).y){
      Int.get(Int.size()-1).x++;
      return;
    }
  }
  Int.add(new PVector(age,kol));
  max = max(max, kol);
}


public void statistic(){
  //for(int i=0; i < kolType; i++) adboot(kolBoot[i],100*kolB[i]/max(kolB[0],1));
  for(int i=0; i < kolType; i++) adboot(kolBoot[i],kolB[i]);
}


public void tablo1(float x, float y, float w, float h)
{
  
  fill(0);
  stroke(50);
  rect(0,0,w,h);
  
  translate(x+100,y+100);
  w-=100;
  h-=200;
  fill(255);
  stroke(255);
  line(0,h,w,h);
  text(age,w-7,h+25);
  line(0,0,0,h);
  
  textAlign(RIGHT);
  //textSize(20);
  textSize(20);
  
  float k = max/h;
  
  for(int i = 0; i <= h; i+=h/10){
    text(PApplet.parseInt(i*k),-5,h-i);
  }
  
  strokeWeight(1);
  stroke(colorType[0]);
  drawLine(kolBoot[0],w,h,k);
  
  strokeWeight(3);
 
 for(int i = 1; i<kolType; i++){
   stroke(colorType[i]);
   drawLine(kolBoot[i],w,h,k);
 }
 
  strokeWeight(0);

  //translate(-x,-y);
}

public void drawLine(ArrayList<PVector> arr ,float w, float h, float k){
  if(arr.size()==0)return;
    float maxX=arr.get(arr.size()-1).x;
  for(int i = 0; i < arr.size()-1; i++){
    line(arr.get(i).x*w/maxX,h-arr.get(i).y/k,arr.get(i+1).x*w/maxX,h-arr.get(i+1).y/k);
  }
}


public void tablo2(){
  background(25);
  fill(255);
  
  textSize(40);
  textAlign(LEFT);
  
  text("max number of boots: "+max, 50, 100);
  
  text("number of boots: "+boot.size(), 50, 175);
  
  float grass=0;
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      grass+=SOIL[i][j].hp;
    }
  }
  //grass=grass/(grassMaxHP*worldWidth*worldHeight);
  
  textSize(50);
  text("Grass",width-325,height-325, 175);
  
  int[] c = {color(0,0,0),color(0,255,0)};
  diagram(width-250,height-180,250,c,PApplet.parseInt(grassMaxHP*worldWidth*worldHeight),PApplet.parseInt(grass));
  
  textSize(50);
  fill(255);
  text(" Boot",width-325,100, 175);
  
  if(boot.size()>0)diagram(width-250,250,250,colorType, kolB.clone());
}


public void diagram(int x, int y, int r, int[] c, int... param){
  float sum = param[0];
  //for(int i = 1; i < param.length; i++ ) sum += param[i];
  
  float rad = 0;
  
  fill(50);
  ellipse(x,y,r,r);
  
  for(int i=1; i< param.length; i++){
    float ts = r*sqrt(abs(min(param[i]/sum,0.4f)))/3+0.1f;
    textSize( ts );
    
    fill(c[i]);
    arc(x, y, r, r, rad, rad + param[i]/sum *TWO_PI);
    
    fill(0);
    float fi = rad + (param[i]/sum *TWO_PI)/2;
    text(PApplet.parseInt(param[i]/sum*100)+"%",x-ts*0.8f + cos(fi)*r/3.5f,y + ts/3 + sin(fi)*r/3.5f);
    
    rad+=param[i]/sum *TWO_PI;
  }
}




ArrayList<Boot> boot = new ArrayList<Boot>();
Soil[][] SOIL;
int worldWidth, worldHeight;
float worldX, worldY;

  
public void generateWorld(float wX, float wY, float x, float y)
{
  boot.clear();
  resetStatistic();
  worldWidth = PApplet.parseInt(x/SIZE);
  worldHeight= PApplet.parseInt(y/SIZE);
  SOIL = new Soil[worldWidth][worldHeight];
  worldX = wX;
  worldY = wY;
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      SOIL[i][j] = new Soil(i,j);
      SOIL[i][j].hp = grassStartHP;
    }
  }
}

public void spawnBoot(String name, int kol, float hp)
{
  boolean threadON = false;
  if(world.active){
    threadON = true;
    threadStop();
  }
  
  for(int i =0; i<kol; i++){
    
    try{
      
      Class c = Class.forName("BootNeurons$" + name);
      Constructor con = c.getConstructor(new Class[] { BootNeurons.class });
      
      Boot b = (Boot) con.newInstance(this);//this -> BootNeurons
      
      b.setCord(PApplet.parseInt(random(worldWidth)),PApplet.parseInt(random(worldHeight)));
      b.setHp(hp);
      boot.add(b);
      
    } catch(ReflectiveOperationException e){println(e);}
  
  }
  
  if(threadON){
    threadStart();
  }
  
}
   
    /*
    //получаем Class
    Class c = Class.forName(String);
    Class c = obj.getClass();
    
    //имя класса
    c.getName();
    
    //конструкторы и их поля, поля типа Class
    Constructor[] constructors = c.getConstructors();
    Class[] paramTypes = constructor.getParameterTypes(); 
    
    //Получаем конкретный конструктор,  передаём массив Class
    Constructor con = c.getConstructor(new Class[] { Integer.class, Boolean.clas });
    //вызов конкретного конструктора
    con.newInstance(new Object[] { new Integer(1), Boolean.FALSE });
    
    //вызов конструктора без параметров
    c.newInstance();
    
    
    Class c = Class.forName("BootNeurons$" + name);
    */


public void graph(){
  fill(0,0);
  stroke(50);
  strokeWeight(8);
  rect(worldX-4,worldY-4,worldWidth*SIZE+8,worldHeight*SIZE+8);
  strokeWeight(1);
  //translate(worldX,worldY);
  
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      SOIL[i][j].graph();
    }
  }
  for(int i = 0; i< boot.size(); i++){
    boot.get(i).graph();
  }
  //translate(-worldX,-worldY);
}


public void display()
{
  age++;
  for(int i=0; i<worldWidth; i++){
    for(int j=0; j<worldHeight; j++){
      SOIL[i][j].display();
    }
  }
 for(int i = 0; i< boot.size(); i++){
    boot.get(i).display();
  }
    
  if(boot.size()<1){
    //spawnBootN(100,500);
  }
  statistic();
}


  
  
 
class NeuralNetwork
{
  ArrayList<ArrayList<Neuron>> network = new ArrayList<ArrayList<Neuron>>();
  ArrayList<Neuron> layer = new ArrayList<Neuron>();
  
  int id=0;
  
  NeuralNetwork(){}
  
  public void addNeuron(float deff)
  {
    Neuron n = new Neuron(deff);
    layer.add(n);
    
    n.id=id;
    id++;
  }
  public void addNeuron(float deff, float ... weight)
  {
    Neuron n = new Neuron(deff,network.get(0),weight);
    layer.add(n);
    
    n.id=id;
    id++;
  }
  
  public void addNeuronR(float deff)
  {
    Neuron n = new Neuron(random(-deff,+deff));
    layer.add(n);
    
    n.id=id;
    id++;
  }
  public void addNeuronR(float deff, float weight)
  {
    float[] w = new float[network.get(0).size()];
    for(int i=0; i<network.get(0).size(); i++)
    w[i]=random(-weight,weight);
    
    Neuron n = new Neuron(random(-deff,+deff),network.get(0),w);
    layer.add(n);
    
    
    n.id=id;
    id++;
  }
  
  public void setLayer()
  {
    ArrayList<Neuron> l = new ArrayList<Neuron>();
    l.addAll(layer);
    network.add(0,l);
    layer.clear();
  }
  public void addLayerR(int kolLayer, int kolNeuron, float deff, float weight)
  {
    for(int i=0;i<kolLayer;i++)
    {
      for(int j=0;j<kolNeuron;j++)
      {
        addNeuronR(deff,weight);
      }
      setLayer();
    }
  }
  public void addLayerR(int kolNeuron, float deff)
  {
    for(int j=0;j<kolNeuron;j++)
    {
      addNeuronR(deff);
    }
    setLayer();
  }
  
  ///////////////
  
  public void display()
  {
    
    for(Neuron n : network.get(0))
    n.disp();
    
    for(int i=1; i<network.size(); i++)
    for(Neuron n : network.get(i))
    n.display();
    
  }
  
  ///////////////
  
  public void in(ArrayList<Float> i)
  {
    if(i.size()==network.get(0).size())
      for(int j=0; j<network.get(0).size(); j++)
        network.get(0).get(j).in=i.get(j)+network.get(0).get(j).deff;
    
    else if(i.size()>network.get(0).size())
    {
      println("Error: count of parametrs more");
      println("than count of input neurons.");
    }
    else if(i.size()<network.get(0).size())
    {
      println("Error: count of parametrs less");
      println("than count of input neurons.");
    }
  }
  
  public void in(float ... i)
  {
    if(i.length==network.get(0).size())
      for(int j=0; j<network.get(0).size(); j++)
        network.get(0).get(j).in=i[j]+network.get(0).get(j).deff;
    
    else if(i.length>network.get(0).size())
    {
      println("Error: count of parametrs more");
      println("than count of input neurons.");
      exit();
    }
    else if(i.length<network.get(0).size())
    {
      println("Error: count of parametrs less");
      println("than count of input neurons.");
      exit();
    }
  }
  
  public float out(int i)
  {
    return network.get(network.size()-1).get(i).out;
  }
  
  //////////////
  
  
  public NeuralNetwork clone()
  {
    NeuralNetwork net = new NeuralNetwork();
    
    for(Neuron n : network.get(network.size()-1))
      {
        net.addNeuron(n.deff);
      }
    net.setLayer();
    for(int i = network.size()-2; i>=0; i--)
    {
      for(Neuron n : network.get(i))
      {
        net.addNeuron(n.deff,n.weight.clone());
      }
      net.setLayer();
    }
    
    return net;
  }
  
  
  public void changeWeight(float r)
  {
    for(int i=0; i<network.size(); i++)
    for(Neuron n : network.get(i)){
      n.changeWeight(r);
    }
  }
  
}
  public void settings() {  size(displayWidth,displayHeight); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BootNeurons" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
