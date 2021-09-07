

void tablo2(){
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
  
  color[] c = {color(0,0,0),color(0,255,0)};
  diagram(width-250,height-180,250,c,int(grassMaxHP*worldWidth*worldHeight),int(grass));
  
  textSize(50);
  fill(255);
  text(" Boot",width-325,100, 175);
  
  if(boot.size()>0)diagram(width-250,250,250,colorType, kolB.clone());
}


void diagram(int x, int y, int r, color[] c, int... param){
  float sum = param[0];
  //for(int i = 1; i < param.length; i++ ) sum += param[i];
  
  float rad = 0;
  
  fill(50);
  ellipse(x,y,r,r);
  
  for(int i=1; i< param.length; i++){
    float ts = r*sqrt(abs(min(param[i]/sum,0.4)))/3+0.1;
    textSize( ts );
    
    fill(c[i]);
    arc(x, y, r, r, rad, rad + param[i]/sum *TWO_PI);
    
    fill(0);
    float fi = rad + (param[i]/sum *TWO_PI)/2;
    text(int(param[i]/sum*100)+"%",x-ts*0.8 + cos(fi)*r/3.5,y + ts/3 + sin(fi)*r/3.5);
    
    rad+=param[i]/sum *TWO_PI;
  }
}
