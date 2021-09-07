class BootMy extends Boot{
  
  final static int ID = 2;
  int getID(){
    return ID;
  }
  
  float teemHP = 300; //количество hp, после которого клетка делится
  
  public BootMy(){
  }
  public BootMy(float teemHP){
    this.teemHP = teemHP;
  }
  
  
  void AI(){
    if(kolB[0]-kolB[ID]*2>0)hp+= float(kolB[0]-kolB[ID]*2)/(kolB[0])*bootHanger;
    //if(random(1000)<1)println((kolB[0]-kolB[ID]*2)/(kolB[0])*bootMaxCapture);
    if(kolB[0]-kolB[ID]*2<0)hp+= float(kolB[0]-kolB[ID]*2)/(kolB[0])*bootMaxCapture;
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
  
  void graphAI(){
    fill(255);
    textAlign(100);
    text("My Boot", 50,50);
    text("Teem HP = " + teemHP, 50,150);
  }
  
  void teem(){
    BootMy b = new BootMy(teemHP + random(-1,1));
    b.setHp(hp/2);
    b.setCord(x,y);
    hp/=2;
    boot.add(b);
  }
}
