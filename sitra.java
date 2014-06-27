 public interface Rule<S,T> {
   public boolean check(S source);
   public T build(S source, Transformer t);
   public void setProperties(T target, S source, Transformer t);
 }

 public interface Transformer {
   public Object transform(Object source);
   public List<Object> transformAll(List<Object> sourceObjects);
   public <S,T> T transform(Class<Rule<S,T>> ruleType, S source);
   public <S,T> List<T> transformAll(Class<Rule<S,T>> ruleType, List<S> source);
 }
