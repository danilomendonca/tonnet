package root;

//verificar até qual versão não gráfica tinha esta classe!!!
public class LineController {
  private String line = null;
  private int a, b;

  public LineController() {
  }

  public LineController(String line) {
    this.setLine(line);
  }

  public void setLine(String line) {
    this.line = line;
    this.a = 0;
    this.b = 0;
  }

  public boolean isNext() {
    if (line==null)
      return false;

    if (b + 1 < this.line.length())
      return true;
    else
      return false;
  }

  public String getNextWord() {
    if (this.isNext()) {
      b++;
      this.a = b;
      if (a==1){
        a--;b--;
      }
      b++;
      while ( (line.charAt(b) != ';'))
        b++;
      return line.substring(a, b);
    }
    else
      return null;
  }
}
