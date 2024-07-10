<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入关键字查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="50" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="新闻标题" show-overflow-tooltip ></el-table-column>
        <el-table-column prop="descr" label="新闻简介" show-overflow-tooltip></el-table-column>
        <el-table-column prop="category" label="新闻分类"></el-table-column>
        <el-table-column prop="content" label="新闻内容" width="100"  align="center">

            <template v-slot="scope">
              <el-button  @click="preview(scope.row.content)">查看内容</el-button>
            </template>

        </el-table-column>
        <el-table-column prop="img" label="配图">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px;" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" width="150"></el-table-column>
        <el-table-column prop="readCount" label="阅读量"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="新闻信息" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="新闻标题" prop="title">
          <el-input v-model="form.title" placeholder="新闻标题"></el-input>
        </el-form-item>
        <el-form-item label="新闻简介" prop="descr">
          <el-input v-model="form.descr" placeholder="新闻简介"></el-input>
        </el-form-item>
        <el-form-item label="新闻分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择">
            <el-option
                v-for="item in category"
                :key="item.id"
                :label="item.name"
                :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="配图" prop="img">
          <el-upload
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="新闻内容" prop="content">
          <div id="editor"></div>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="文章内容" :visible.sync="fromVisible1" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="w-e-text">
        <div v-html="content"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible1 = false">关 闭</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
import E from "wangeditor"
import axios from "axios";
export default {
  name: "News",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      category:[],
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      editor: null,
      content: '',
      fromVisible1: false,
    }
  },
  created() {
    this.fetchCategories();
    this.load(1)
  },
  methods: {
    fetchCategories() {// 分页查询
        this.$request.get('/category/selectAll').then(res => {
          if (res.code === '200') {
            this.category = res.data
          } else {
            this.$message.error(res.msg)
          }
        })
      },
    preview(content) {
      this.content = content
      this.fromVisible1 = true
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
      this.setRichText('')
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
      this.setRichText(this.form.content)
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/news/update' : '/news/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/news/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/news/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/news/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImgSuccess(response, file, fileList) {
      this.form.img = response.data
    },
    setRichText(html) {
      this.$nextTick(() => {
        this.editor = new E(`#editor`)
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        }
        this.editor.config.uploadImgParams = {
          type: 'img',
        }
        this.editor.config.zIndex = 0
        this.editor.create()  // 创建
        this.editor.txt.html(html)
      })
    },
  }
}
</script>

<style scoped>

</style>