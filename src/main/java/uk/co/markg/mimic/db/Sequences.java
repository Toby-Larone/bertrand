/*
 * This file is generated by jOOQ.
 */
package uk.co.markg.mimic.db;


import org.jooq.Sequence;
import org.jooq.impl.Internal;


/**
 * Convenience access to all sequences in 
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>usage_id_seq</code>
     */
    public static final Sequence<Integer> USAGE_ID_SEQ = Internal.createSequence("usage_id_seq", DefaultSchema.DEFAULT_SCHEMA, org.jooq.impl.SQLDataType.INTEGER.nullable(false), null, null, null, null, false, null);
}