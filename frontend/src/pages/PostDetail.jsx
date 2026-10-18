import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import api from '../api/axios'
import VoteButtons from '../components/VoteButtons'
import CommentThread from '../components/CommentThread'
import { useAuth } from '../context/AuthContext'

export default function PostDetail() {
  const { id } = useParams()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [text, setText] = useState('')
  const { user } = useAuth()
  const navigate = useNavigate()

  async function load() {
    const { data: p } = await api.get(`/posts/${id}`)
    setPost(p)
    const { data: c } = await api.get(`/comments/post/${id}`)
    setComments(c)
  }

  useEffect(() => { load() }, [id])

  async function handleVote(voteType) {
    if (!user) return navigate('/login')
    await api.post(`/posts/${id}/vote`, { voteType })
    load()
  }

  async function submitComment(e) {
    e.preventDefault()
    if (!user) return navigate('/login')
    await api.post('/comments', { postId: Number(id), content: text })
    setText('')
    load()
  }

  async function deletePost() {
    if (!confirm('Delete this post?')) return
    await api.delete(`/posts/${id}`)
    navigate(`/r/${post.communityName}`)
  }

  if (!post) return <div className="container"><p className="muted">Loading...</p></div>

  return (
    <div className="container">
      <div className="card post-row">
        <VoteButtons score={post.score} currentVote={post.currentUserVote} onVote={handleVote} />
        <div style={{ flex: 1 }}>
          <div className="post-meta">
            r/{post.communityName} · posted by u/{post.authorUsername}
          </div>
          <h2 style={{ margin: '6px 0' }}>{post.title}</h2>
          {post.content && <div className="post-content">{post.content}</div>}
          {user?.username === post.authorUsername && (
            <button className="btn secondary" style={{ marginTop: 10 }} onClick={deletePost}>Delete</button>
          )}
        </div>
      </div>

      <div className="card">
        <form className="stacked" onSubmit={submitComment}>
          <label>Add a comment</label>
          <textarea value={text} onChange={(e) => setText(e.target.value)} required />
          <button className="btn" style={{ alignSelf: 'flex-start', marginTop: 8 }} type="submit">Comment</button>
        </form>
      </div>

      <div className="card">
        <h3>{comments.length} comments</h3>
        {comments.map((c) => (
          <CommentThread key={c.id} comment={c} postId={Number(id)} onChange={load} />
        ))}
      </div>
    </div>
  )
}
