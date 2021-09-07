
class BootDNA extends Boot
{
  final static int ID = 1;
  int getID(){
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
    if(random(100)<15)DNA.set(int(random(DNA.size())), int(random(valueMax/3)));
  }
 
  
  void AI(){
    AI(0);
    if(random(1000)<1)teem();
  }
  
  void AI(int iteration){
    
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
      int hp = DNA.get(index) * int(grassMaxHP / valueMax);
      
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
  
  
  void newAI(){
    for(int i=0; i<valueMax; i++){
      DNA.add(int(random(valueMax/10)));
    }
  }
  
  void graphAI(){
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
  
  
  void teem(){
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
