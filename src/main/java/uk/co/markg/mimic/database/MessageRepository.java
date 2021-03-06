package uk.co.markg.mimic.database;

import static org.jooq.impl.DSL.count;
import static uk.co.markg.mimic.db.tables.Messages.MESSAGES;
import java.util.List;
import java.util.stream.Stream;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import disparse.parser.reflection.Injectable;
import net.dv8tion.jda.api.entities.Message;
import uk.co.markg.mimic.db.tables.pojos.Messages;
import uk.co.markg.mimic.db.tables.records.MessagesRecord;

/**
 * A {@link org.jooq.DSLContext DSLContext} implementation to access the
 * {@link uk.co.markg.mimic.db.tables.Messages Messages} table generated by JOOQ.
 */
public class MessageRepository {

  private static final int HIGH_CAPACITY_LIMIT = 50_000;
  private DSLContext dsl;

  /**
   * {@link disparse.parser.reflection.Injectable Injectable} method used by disparse upon command
   * invocation.
   * 
   * @return a new message repository instance
   */
  @Injectable
  public static MessageRepository getRepository() {
    return new MessageRepository();
  }

  private MessageRepository() {
    dsl = JooqConnection.getJooqContext();
  }

  /**
   * Create a {@link uk.co.markg.mimic.db.tables.pojos.Messages Messages} instance from a userid and
   * a discord message and saves it to the database
   * 
   * @param userid  The userid of the message author
   * @param message The discord message object
   * @return The number of rows inserted
   */
  public int save(long userid, Message message) {
    var msg = new Messages(message.getIdLong(), userid, message.getContentRaw(),
        message.getChannel().getIdLong(), message.getGuild().getIdLong());
    return save(msg);
  }

  /**
   * Save a {@link net.dv8tion.jda.api.entities.Message Message} into the database
   * 
   * @param message The message to be saved
   * @return The number of rows inserted
   */
  public int save(Messages message) {
    return dsl.executeInsert(dsl.newRecord(MESSAGES, message));
  }

  /**
   * Returns a list of {@link net.dv8tion.jda.api.entities.Message Messages} as strings belonging to
   * all userids in the list
   * 
   * @param userids The userids to get the messages for
   * @return The list of messages
   */
  public List<String> getByUsers(List<Long> userids, long serverid) {
    return dsl.select(MESSAGES.CONTENT).from(MESSAGES).where(MESSAGES.USERID.in(userids))
        .and(MESSAGES.SERVERID.in(serverid)).fetchInto(String.class);
  }

  public Stream<String> getByServerid(long server) {
    return dsl.select(MESSAGES.CONTENT).from(MESSAGES).where(MESSAGES.SERVERID.eq(server))
        .orderBy(MESSAGES.MESSAGEID.desc()).fetchStreamInto(String.class);
  }

  public Stream<String> getByServeridFromMessage(long server, long lastMessageId) {
    return dsl.select(MESSAGES.CONTENT).from(MESSAGES).where(MESSAGES.SERVERID.eq(server))
        .and(MESSAGES.MESSAGEID.gt(lastMessageId)).orderBy(MESSAGES.MESSAGEID.desc())
        .fetchStreamInto(String.class);
  }
  
  public Long getLatestServerMessage(long server) {
    return dsl.select(MESSAGES.MESSAGEID).from(MESSAGES).orderBy(MESSAGES.MESSAGEID.desc()).limit(1).fetchOneInto(Long.class);
  }

  public Stream<String> getByUserid(long user, long server) {
    return dsl.select(MESSAGES.CONTENT).from(MESSAGES).where(MESSAGES.USERID.eq(user))
        .and(MESSAGES.SERVERID.eq(server)).orderBy(MESSAGES.MESSAGEID.desc())
        .fetchStreamInto(String.class);
  }

  public List<Long> getHighCapacityServers() {
    return dsl.select(MESSAGES.SERVERID).from(MESSAGES).groupBy(MESSAGES.SERVERID)
        .having(count().gt(HIGH_CAPACITY_LIMIT)).fetchInto(Long.class);
  }

  public List<Long> getHighCapacityUsers(long serverid) {
    return dsl.select(MESSAGES.USERID).from(MESSAGES).where(MESSAGES.SERVERID.eq(serverid))
        .groupBy(MESSAGES.USERID).having(count().gt(HIGH_CAPACITY_LIMIT)).fetchInto(Long.class);
  }

  public int getCount(long serverid) {
    return dsl.selectCount().from(MESSAGES).where(MESSAGES.SERVERID.eq(serverid)).fetchOne(0,
        int.class);
  }

  public int getUniqueWordCount(long serverid) {
    Field<?> field = DSL.field("regexp_split_to_table(content, E'\\\\s+\\\\v?')");
    Table<?> wordTable =
        dsl.selectDistinct(field).from(MESSAGES).where(MESSAGES.SERVERID.eq(serverid)).asTable();
    return dsl.select(DSL.count()).from(wordTable).fetchOne(0, int.class);
  }

  /**
   * 
   */
  public int getCountByUserId(long userid, long serverid) {
    return dsl.selectCount().from(MESSAGES).where(MESSAGES.USERID.eq(userid))
        .and(MESSAGES.SERVERID.eq(serverid)).fetchOne(0, int.class);
  }

  /**
   * Batch inserts a list of {@link uk.co.markg.mimic.db.tables.records.MessagesRecord
   * MessageRecord}
   * 
   * @param batch the batch of messages to insert
   */
  public void batchInsert(List<MessagesRecord> batch) {
    dsl.batchInsert(batch).execute();
  }

  /**
   * Deletes a message by its message id from the database if it exists
   * 
   * @param messageid the messageid of the message to delete
   * @return the number of rows deleted
   */
  public int deleteById(long messageid) {
    return dsl.deleteFrom(MESSAGES).where(MESSAGES.MESSAGEID.eq(messageid)).execute();
  }

  public int deleteByChannelId(long channelid) {
    return dsl.deleteFrom(MESSAGES).where(MESSAGES.CHANNELID.eq(channelid)).execute();
  }

  public int edit(long messageid, Message newMessage) {
    return dsl.update(MESSAGES).set(MESSAGES.CONTENT, newMessage.getContentRaw())
        .where(MESSAGES.MESSAGEID.eq(messageid)).execute();
  }

}
