package afn.xloop.booktracker_cnsd23;

import java.util.Collection;

public interface IBookRepository<M> {
    public void save(M m);
    public Collection<M> getAllBook();

    public void delete(M m);

    public void update(M m);
}