
class BootN2 extends Boot{
  
  final static int ID = 4;
  int getID(){
    return ID;
  }
  
  NeuralNetwork net = new NeuralNetwork();
  
  int RANGE = 1;
  
  public BootN2(){
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
  
  
  
  void newAI()
  {
    net = new NeuralNetwork();
  
    net.addLayerR(6,1);
    net.addLayerR(1,int(sq(RANGE*2+1))+1,10,1);
    
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
