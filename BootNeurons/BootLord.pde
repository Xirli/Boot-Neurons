
class BootLord extends Boot{
  
  final static int ID = 5;
  int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  public BootLord(){
    newAI();
  }

  
  float sumSoil(Soil s)
  {
    float sumHp = 0;
    for(int i=-1; i<=1; i++){
      for(int j=-1; j<=1; j++){
        sumHp+= s.getSoil(i,j).hp;
      }
    }
    return sumHp;
  }
  
  
  void AI()
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
  
  
  
  void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,11,10,1);
  }
  void newAI(NeuralNetwork n)
  {
    net = n.clone();
    if(random(20)<1 )net.changeWeight(1.25);
    if(random(20)<15)net.changeWeight(0.01);
  }
  
  void graphAI(){
    
  }
  
  
  void teem(){
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
