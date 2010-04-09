package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Persistable
{
	public void persist(DataOutputStream dos) throws IOException;
	public void restore(DataInputStream dis) throws IOException;
}
