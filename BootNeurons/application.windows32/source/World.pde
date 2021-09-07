
import java.lang.reflect.*;


ArrayList<Boot> boot = new ArrayList<Boot>();
Soil[][] SOIL;
int worldWidth, worldHeight;
float worldX, worldY;

  
void generateWorld(float wX, float wY, float x, float y)
{
  boot.clear();
  resetStatistic();
  worldWidth = int(x/SIZE);
  worldHeight= int(y/SIZE);
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

void spawnBoot(String name, int kol, float hp)
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
      
      b.setCord(int(random(worldWidth)),int(random(worldHeight)));
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


void graph(){
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


void display()
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


  
  
 
