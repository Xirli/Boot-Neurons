
class BootN extends Boot{
  
  final static int ID = 3;
  int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  int RANGE = 1;
  
  public BootN(){
    newAI();
  }
  
  
  void autoIn(int range)
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
  
  
  
  void AI()
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
  
  
  void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,int(sq(RANGE*2+1))+1,100,1);
    
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
