package io.descoped.container.info;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by oranheim on 05/02/2017.
 */
public class TreeNodeTest {

    private static final Logger log = LoggerFactory.getLogger(TreeNodeTest.class);

    private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    StringBuilder builder = new StringBuilder();

    private boolean isLastElement(TreeNode<KeyValue> treeNode) {

        return false;
    }

    private void walk(TreeNode<KeyValue> node) {

        boolean hasMoreElements = (node.getParent() == null ? false : node.getParent().getChildren().indexOf(node) == node.getParent().getChildren().size()-1);
        String comma = (!hasMoreElements ? "," : "");

        if (node.isLeaf()) {
            builder.append(createIndent(node.getLevel())).append("\"").append(node.getData().getKey()).append("\": \"").append(node.getData().getValue()).append("\"").append(comma).append("\n");
        }

        if (!node.isRoot() && node.isGroup()) {
            builder.append(createIndent(node.getLevel())).append("\"").append(node.getData().getKey()).append("\": ").append("{\n");
        }

        List<TreeNode<KeyValue>> children = node.getChildren();
        for (Iterator<TreeNode<KeyValue>> it = children.iterator(); it.hasNext(); ) {
            TreeNode<KeyValue> child = it.next();
            walk(child);
        }

        if (!node.isRoot() && node.isGroup()) {

            builder.append(createIndent(node.getLevel())).append("}").append(comma).append("\n");
        }
    }

    @Test
    public void testTreeNodes() throws Exception {
        TreeNode<KeyValue> treeNodeRoot = new TreeNode<>();
        TreeNode<KeyValue> g0 = treeNodeRoot.add(new KeyValue("g0.1", "v0.1"));

        TreeNode<KeyValue> g1 = treeNodeRoot.add(new KeyValue("g1"));
        TreeNode<KeyValue> g11 = g1.add(new KeyValue("g1.1", "v1.1"));
        TreeNode<KeyValue> g12 = g1.add(new KeyValue("g1.2", "v1.2"));
        TreeNode<KeyValue> foo = g12.add(new KeyValue("foo"));
        TreeNode<KeyValue> bar = foo.add(new KeyValue("bar", "baz"));

        TreeNode<KeyValue> g2 = treeNodeRoot.add(new KeyValue("g2"));
        TreeNode<KeyValue> g21 = g2.add(new KeyValue("g2.1", "v2.1"));
        TreeNode<KeyValue> g22 = g2.add(new KeyValue("g2.2", "v2.2"));

        TreeNode<KeyValue> g3 = treeNodeRoot.add(new KeyValue("g3"));

        walk(treeNodeRoot);

        log.trace("JSON:\n{}", String.format("{\n%s}", builder.toString()));
    }

}
