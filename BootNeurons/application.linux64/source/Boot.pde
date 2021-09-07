abstract class Boot
{
  
  int x,y;
  int direction=0;
  
  float hp;
  
  abstract void teem();  //размножение клеток
  abstract void AI();    //интелект, решает куда ити и когда размножаться
  abstract void graphAI(); //"изображает свой интелект"
  
  abstract int getID();//каждый вид ботов имеет свой id

  public Boot(){
    kolB[getID()]++;
    kolB[0]++;
  }
  
  void setCord(int x, int y)
  {
    this.x=x;
    this.y=y;
  }
  
  void setHp(float hp){
    this.hp=hp;
  }
 
  Soil getSoil(int x, int y)
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
  void setDirect(int x, int y)
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
  
  void display()
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
  
  void graph()
  {
    stroke(colorType[getID()]);
    fill(colorType[getID()]);
    rect(x*SIZE,y*SIZE,SIZE,SIZE);
  }
  
  void death(){
    boot.remove(this);
    kolB[getID()]--;
    kolB[0]--;
    
    SOIL[x][y].hp+=grassMaxHP/10;
  }

}
