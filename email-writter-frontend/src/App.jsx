import { Box, CircularProgress, Container, FormControl, InputLabel, MenuItem, Select, TextField, Typography ,Button} from '@mui/material'
import './App.css'
import { useState } from 'react'
import axios from 'axios';

function App() {
 const[emailcontent,setEmailContent]=useState('');
const[tone,setTone]=useState('');
const[loading,setLoading]=useState(false);
const[generateReply,setGenerateReply]=useState('');
const handleSubmit=async()=>{

  setLoading(true);
  try {
    const response=await axios.post("http://localhost:8080/api/email/generate",{

   emailcontent,
   tone

    });
    setGenerateReply(typeof response.data==='string'?
      response.data:JSON.stringify(response.data)
    );
  } catch (error) {
    
  }
  finally{
    setLoading(false);
  }

};
return (
    <Container maxWidth="md" sx={{py:4}}>

      <Typography variant='h3' component="h1" gutterBottom>
        Email Reply generator
      </Typography>
    <Box sx={{mx:3}}>
      <TextField
          fullWidth
          multiline
          rows={6}
          variant='outlined'
         label="original Email content"
          value={emailcontent||''}
          onChange={(e)=>setEmailContent(e.target.value)}
          sx={{mb:2}}
      />
     
      <FormControl fullWidth sx={{mb:2}}>
  <InputLabel>Tone(optional)</InputLabel>
  <Select
   
    value={tone||''}
    label="Tone(optional)"
    onChange={(e)=>setTone(e.target.value)}
  >
    <MenuItem value="None">None</MenuItem>
    <MenuItem value="Professional">Professional</MenuItem>
    <MenuItem value="Casual">Casual</MenuItem>
    <MenuItem value="Friendly">Friendly</MenuItem>
  </Select>
</FormControl>
<Button variant="contained" 
sx={{mb:2}}
onClick={handleSubmit} disabled={!emailcontent||loading}>
 {loading?<CircularProgress size={24}/>:"Generate Reply"}
</Button>
    </Box>
    
<Box sx={{mx:3}}>
      <TextField
          fullWidth
          multiline
          rows={6}
          variant='outlined'
          value={generateReply||''}
          inputProps={{ readOnly: true }}
          sx={{mb:2}}
      />
 <Button 
  variant="outlined" 
  onClick={() => navigator.clipboard.writeText(generateReply)}>

  Copy to ClipBoard
</Button>
</Box>

    </Container>
  )
}

export default App
