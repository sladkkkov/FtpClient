package com.example.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataCommunicator;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchyMapper;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Route("scroll-to-item-in-tree-grid")
public class ScrollToItemInTreeGrid extends VerticalLayout {

    public static class ScrollTreeGrid<T> extends TreeGrid<T> {

        public ScrollTreeGrid(Class<T> beanType) {
            super(beanType);
            initScrollWhenReady();
        }

        public ScrollTreeGrid() {
            super();
            initScrollWhenReady();
        }

        public ScrollTreeGrid(HierarchicalDataProvider<T, ?> dataProvider) {
            super(dataProvider);
            initScrollWhenReady();
        }

        /**
         * The method for scrolling to an item. Takes into account lazy loading nature
         * of grid and does the scroll operation only until the grid has finished
         * loading data
         *
         * @param item the item where to scroll to
         */
        public void scrollToItem(T item) {
            int index = getIndexForItem(item);
            if (index >= 0) {
                this.getElement().executeJs("this.scrollWhenReady($0, true);", index);
            }
        }

        /**
         * This is a method for getting the row index of an item in a treegrid. This
         * works but is prone to break in the future versions due to its usage of
         * reflection to access private methods to get access to the index.
         */
        private int getIndexForItem(T item) {
            HierarchicalDataCommunicator<T> dataCommunicator = super.getDataCommunicator();
            Method getHierarchyMapper = null;
            try {
                getHierarchyMapper = HierarchicalDataCommunicator.class.getDeclaredMethod("getHierarchyMapper");
                getHierarchyMapper.setAccessible(true);
                HierarchyMapper<T, ?> mapper = (HierarchyMapper) getHierarchyMapper.invoke(dataCommunicator);
                return mapper.getIndex(item);
            } catch (Exception e) {
                // TODO: handle
                e.printStackTrace();
            }
            return -1;
        }

        private void initScrollWhenReady() {
            runBeforeClientResponse(
                    ui ->
                            getElement()
                                    .executeJs(
                                            "this.scrollWhenReady = function(index, firstCall){" +
                                                    "if(this.loading || firstCall) {var that = this; setTimeout(function(){that.scrollWhenReady(index, false);}, 200);}" +
                                                    "        else {this.scrollToIndex(index);}" +
                                                    "};"
                                    )
            );
        }

        private void runBeforeClientResponse(SerializableConsumer<UI> command) {
            getElement().getNode().runWhenAttached(ui -> ui.beforeClientResponse(this, context -> command.accept(ui)));
        }
    }


    /**
     * Demo
     */
    public ScrollToItemInTreeGrid() throws IOException {
        ScrollTreeGrid<FtpFile> grid = new ScrollTreeGrid<>();
        grid.addHierarchyColumn(FtpFile::getName).setHeader("Name");
        grid.addColumn(FtpFile::getSize).setHeader("Size");


        FtpServiceNumberTwo ftpService = new FtpServiceNumberTwo("91.222.128.11", 21, "testftp_guest", "12345");
        ftpService.open();
        ftpService.getRootItems();
        List<FtpFile> list1 = ftpService.getList();
     /*   FtpFile ftp1 = new FtpFile("g", 26L, null, true);
        FtpFile ftp2 = new FtpFile("g1", 26L, ftp1, true);
        FtpFile ftp3 = new FtpFile("g2", 26L, ftp2, true);
        List<FtpFile> list = new ArrayList<>();
        list.add(ftp1);
        list.add(ftp3);
        list.add(ftp2);


        for (FtpFile ftpFile : list1) {
            System.out.println(ftpFile.getName());
        }*/

        list1.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
        add(grid);

    }
}

        