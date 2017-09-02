package io.descoped.container.info;

import java.util.Iterator;
import java.util.List;

/**
 * Created by oranheim on 04/02/2017.
 */
public class InfoBuilder {

    private static final ThreadLocal<StringBuilder> stringBuilderLocal = ThreadLocal.withInitial(() -> new StringBuilder());
//    private static final Logger log = LoggerFactory.getLogger(InfoBuilder.class);
    private TreeNode<KeyValue> root = new TreeNode<>();
    private TreeNode<KeyValue> current;

    private InfoBuilder() {
        current = root;
    }

    public static InfoBuilder builder() {
        stringBuilderLocal.remove();
        return new InfoBuilder();
    }

    public InfoBuilder key(String key) {
        TreeNode<KeyValue> keyNode = current.add(new KeyValue(key));
        current = keyNode;
        return this;
    }

    public InfoBuilder keyValue(String key, String value) {
        current.add(new KeyValue(key, value));
        return this;
    }

    public InfoBuilder up() {
        current = current.getParent();
        return this;
    }

    public InfoBuilder dump() {
        return dump(root);
    }

    private String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private InfoBuilder dump(TreeNode<KeyValue> treeRoot) {
        String indent = createIndent(treeRoot.getLevel());
        System.out.println(indent + "(" + treeRoot.getLevel() + ") " + (treeRoot.getData() == null ? "[ROOT]" : treeRoot.getData()));
        for (TreeNode<KeyValue> node : treeRoot.getChildren()) {
            dump(node);
        }
        return this;
    }

    private StringBuilder buffer() {
        return stringBuilderLocal.get();
    }

    private void indent(TreeNode<KeyValue> node) {
        int depth = node.getLevel();
        for (int n = 0; n < depth; n++) {
            buffer().append("\t");
        }
    }

    private String appendComma(boolean hasMoreElement) {
        return (hasMoreElement ? "" : ",");
    }

    private void appendLeaf(TreeNode<KeyValue> node, boolean hasMoreElement) {
        indent(node);
        buffer().append("\"").append(node.getData().getKey()).append("\": \"").append(node.getData().getValue()).append("\"").append(appendComma(hasMoreElement)).append("\n");
    }

    private void appendGroup(TreeNode<KeyValue> node) {
        indent(node);
        buffer().append("\"").append(node.getData().getKey()).append("\": ").append("{\n");
    }

    private void appendGroupTail(TreeNode<KeyValue> node, boolean hasMoreElement) {
        indent(node);
        buffer().append("}").append(appendComma(hasMoreElement)).append("\n");
    }

    private void walk(TreeNode<KeyValue> node) {
        boolean hasMoreElements = (node.getParent() == null ? false : node.getParent().getChildren().indexOf(node) == node.getParent().getChildren().size() - 1);

        if (node.isLeaf()) {
            appendLeaf(node, hasMoreElements);
        }

        if (!node.isRoot() && node.isGroup()) {
            appendGroup(node);
        }

        List<TreeNode<KeyValue>> children = node.getChildren();
        for (Iterator<TreeNode<KeyValue>> it = children.iterator(); it.hasNext(); ) {
            TreeNode<KeyValue> child = it.next();
            walk(child);
        }

        if (!node.isRoot() && node.isGroup()) {
            appendGroupTail(node, hasMoreElements);
        }
    }

    public String build() {
        walk(root);
        return String.format("{\n%s}", buffer().toString());
    }

}
