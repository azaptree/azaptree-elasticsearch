package test.com.azaptree.elasticsearch

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.azaptree.logging.Slf4jLogger
import com.azaptree.application.ApplicationService
import org.scalatest.BeforeAndAfterAll
import ElasticSearchQuickTest._
import com.azaptree.application.component.Component
import com.azaptree.application.component.ComponentNotConstructed
import org.elasticsearch.node.Node
import com.azaptree.elasticsearch.componentLifeCycles.ElasticNodeLifeCycle
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequestBuilder
import org.elasticsearch.common.xcontent.XContentFactory
import com.azaptree.utils.GUID
import java.util.Date
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder

object ElasticSearchQuickTest {

  val app = new ApplicationService()

  val esNodeLifeCycle = ElasticNodeLifeCycle(client = Some(true))
  val esNodeComp = Component[ComponentNotConstructed, Node](name = "elasticsearch-node", componentLifeCycle = esNodeLifeCycle)
  val esNode = app.registerComponent(esNodeComp).get
  val esClient = esNode.client()

}

class ElasticSearchQuickTest extends FunSuite with ShouldMatchers with Slf4jLogger with BeforeAndAfterAll {

  override def afterAll() = {
    app.stop()
  }

  test("ElasticSearch client can be created and attach to a running cluster") {
    val adminClient = esClient.admin()
    val cluster = adminClient.cluster()

    val nodesInfoResponseFuture = new NodesInfoRequestBuilder(cluster).all().execute()
    val nodesInfoResponse = nodesInfoResponseFuture.get()
    val nodes = nodesInfoResponse.getNodes()
    log.info("nodes.length = {}", nodes.length)
    nodes.foreach { node =>
      log.info("http bound address = {}", node.getHttp().address().boundAddress())
      log.info("http publish address = {}", node.getHttp().address().publishAddress())
    }

    val jsonBuilder = XContentFactory.jsonBuilder()
    val infoLevel: Byte = 1
    val guid = GUID().guid
    jsonBuilder.startObject()
      .field("id", guid)
      .field("logger", "test.com.azaptree.elasticsearch.ElasticSearchQuickTest")
      .field("createdOn", new Date())
      .field("level", infoLevel)
      .field("msg", "This is a test message")
      .endObject()

    log.info("json message : {}", jsonBuilder.string())

    val indexResult = esClient.prepareIndex("log", "log", guid)
      .setSource(jsonBuilder)
      .execute().actionGet()

    log.info("indexResult.getId() = {}", indexResult.getId())
    log.info("indexResult.getVersion() = {}", indexResult.getVersion())
    log.info("indexResult.getType() = {}", indexResult.getType())

  }

}