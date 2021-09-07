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
