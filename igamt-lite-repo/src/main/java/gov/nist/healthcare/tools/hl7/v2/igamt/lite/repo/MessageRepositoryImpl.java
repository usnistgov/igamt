package gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;

public class MessageRepositoryImpl implements MessageOperations {
  private Logger log = LoggerFactory.getLogger(MessageRepositoryImpl.class);

  @Autowired
  private MongoOperations mongo;

  @Override
  public List<Message> findByIds(Set<String> ids) {
    Criteria where = Criteria.where("id").in(ids);
    Query qry = Query.query(where);
    List<Message> messages = mongo.find(qry, Message.class);
    return messages;
  }

  @Override
  public List<Message> findByNamesScopeAndVersion(String name, String structId, String scope,
      String hl7Version) {
    Criteria where = Criteria.where("event").is(name);
    where.andOperator(Criteria.where("hl7Version").is(hl7Version),
        Criteria.where("scope").is(scope), Criteria.where("structID").is(structId));
    Query qry = Query.query(where);

    return mongo.find(qry, Message.class);
  }

  @Override
  public Message findByStructIdAndScopeAndVersion(String structId, String scope,
      String hl7Version) {
    Criteria where = Criteria.where("structID").is(structId);
    where.andOperator(Criteria.where("hl7Version").is(hl7Version),
        Criteria.where("scope").is(scope));
    Query qry = Query.query(where);
    if (!mongo.find(qry, Message.class).isEmpty()) {
      return mongo.find(qry, Message.class).get(0);
    } else {
      return null;
    }

  }

  @Override
  public Date updateDate(String id, Date date) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    query.fields().include("dateUpdated");
    Update update = new Update();
    update.set("dateUpdated", date);
    mongo.updateFirst(query, update, Message.class);
    return date;
  }
}
