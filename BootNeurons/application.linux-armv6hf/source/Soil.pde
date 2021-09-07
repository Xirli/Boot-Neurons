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
  void display()
  {
    
    
    if(random(1)<(1-speedRigor)){
      return;
    }
    
    else if(hp<grassMaxHP){
      active = true;
      float sum = getSoil(-1,0).hp + getSoil(1,0).hp + getSoil(0,-1).hp + getSoil(0,1).hp;
      //float sum = 30;
      hp+=sum*0.002/sqrt(speedRigor)*grassSpeed * 2*y/worldHeight/(hp+1);
      hp=min(hp,grassMaxHP);
    }
  }
  
  Soil getSoil(int x, int y)
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

  void graph()
  {
    if(!active)return;
    active = false;
    
    stroke(0,255*hp/grassMaxHP,125*(1-hp/grassMaxHP));
    fill  (0,255*hp/grassMaxHP,125*(1-hp/grassMaxHP));
    rect(x*SIZE,y*SIZE,SIZE,SIZE);
  }
  
}
